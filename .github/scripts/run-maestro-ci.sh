#!/usr/bin/env bash
#
# Installs the debug APK on the running emulator and executes the ci-tagged Maestro
# flows, collecting everything needed to triage a failure.
#
# WHY THIS IS A FILE AND NOT AN INLINE `script:` BLOCK
# ---------------------------------------------------
# reactivecircus/android-emulator-runner does not hand the `script:` input to a shell as
# one program. src/script-parser.ts splits it on newlines, drops blank and #-comment
# lines, and runs each remaining line through its own `sh -c`. So an inline block:
#
#   * loses every `\` line continuation - `maestro test .maestro \` runs on its own with
#     no flags at all (i.e. every flow, not just the ci-tagged ones), and the following
#     lines fail as `--include-tags: command not found`;
#   * cannot keep shell state - `set -o pipefail` and `status=0` exit with the shell that
#     ran them, so `|| status=$?` lands in a fresh shell as a syntax error.
#
# Every example in the action's README is a single command for exactly this reason. One
# line in the workflow, real shell semantics in here.

set -euo pipefail

ARTIFACTS="maestro-artifacts"
APK="app/build/outputs/apk/debug/app-debug.apk"

mkdir -p "$ARTIFACTS/logs" "$ARTIFACTS/reports" "$ARTIFACTS/debug" "$ARTIFACTS/test-output"

adb wait-for-device

# -d permits reinstalling over an already-installed build.
adb install -r -d "$APK"

# Clear the ring buffer so the dump at the end covers only this run.
adb logcat -c

# Only ci-tagged flows run here. flow.yaml, create-three-pickups.yaml and login.yaml all
# need a real Firebase phone sign-in that an ephemeral CI emulator cannot perform, and
# create-three-pickups.yaml writes to production Firestore.
#
#   --test-output-dir      screenshots + video (NOT covered by --debug-output)
#   --debug-output         maestro.log + commands JSON + screen hierarchies
#   --flatten-debug-output no per-run timestamped subfolders (docs: "Useful for CI")
#   --no-ansi              keeps escape codes out of the log and the artifacts
#
# `|| status=$?` is exempt from errexit, so a test failure does not skip the logcat dump
# below. pipefail is what makes the pipeline report Maestro's status rather than tee's.
status=0
maestro test .maestro \
  --include-tags ci \
  --format junit \
  --output "$ARTIFACTS/reports/maestro-report.xml" \
  --test-output-dir "$ARTIFACTS/test-output" \
  --debug-output "$ARTIFACTS/debug" \
  --flatten-debug-output \
  --no-ansi 2>&1 | tee "$ARTIFACTS/logs/maestro.log" || status=$?

# Dumped either way, so crashes and ANRs stay triageable on a green run too.
adb logcat -d > "$ARTIFACTS/logs/logcat.txt" 2>&1 || true

exit "$status"
