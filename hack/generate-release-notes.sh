#!/usr/bin/env bash
#
# Builds a human-readable changelog for the current release tag by grouping
# conventional-commit subjects between the previous tag and now.
#
# Usage: ./hack/generate-release-notes.sh [output-file]
#
# In CI, GITHUB_REF_NAME and GITHUB_REPOSITORY are provided by the runner.
set -euo pipefail

output="${1:-release-notes.md}"
tag="${GITHUB_REF_NAME:-$(git describe --tags --abbrev=0)}"

if [ -n "${GITHUB_REPOSITORY:-}" ]; then
  repository="$GITHUB_REPOSITORY"
else
  repository="$(git remote get-url origin)"
  repository="${repository##*github.com}"
  repository="${repository#[:/]}"
  repository="${repository%.git}"
fi

previous_tag="$(git describe --tags --abbrev=0 "${tag}^" 2>/dev/null || true)"
if [ -n "$previous_tag" ]; then
  range="${previous_tag}..${tag}"
  compare_url="https://github.com/${repository}/compare/${previous_tag}...${tag}"
else
  range="${tag}"
  compare_url="https://github.com/${repository}/commits/${tag}"
fi

subjects="$(git log "$range" --no-merges --pretty=format:%s)"
emitted=0

emit_section() {
  local heading="$1"
  local pattern="$2"
  local lines
  lines="$(printf '%s\n' "$subjects" | grep -E "$pattern" || true)"
  if [ -z "$lines" ]; then
    return
  fi
  emitted=$((emitted + 1))
  printf '### %s\n\n' "$heading"
  printf '%s\n' "$lines" \
    | sed -E 's/^[a-zA-Z]+(\([^)]*\))?!?: //' \
    | awk '{ print toupper(substr($0, 1, 1)) substr($0, 2) }' \
    | sed -E 's/^/- /'
  printf '\n'
}

known_types='^(feat|fix|perf|refactor|docs|test|chore|build|ci|style)(\([^)]*\))?!?:'

{
  printf '## What changed in %s\n\n' "$tag"

  if [ -n "$subjects" ]; then
    emit_section "Features" '^feat(\([^)]*\))?!?:'
    emit_section "Fixes" '^fix(\([^)]*\))?!?:'
    emit_section "Performance" '^perf(\([^)]*\))?!?:'
    emit_section "Refactoring" '^refactor(\([^)]*\))?!?:'
    emit_section "Documentation" '^docs(\([^)]*\))?!?:'
    emit_section "Tests" '^test(\([^)]*\))?!?:'
    emit_section "Maintenance" '^(chore|build|ci|style)(\([^)]*\))?!?:'

    if [ "$emitted" -eq 0 ]; then
      printf '### Changes\n\n'
      printf '%s\n' "$subjects" | sed -E 's/^/- /'
      printf '\n'
    else
      local_other="$(printf '%s\n' "$subjects" | grep -vE "$known_types" || true)"
      if [ -n "$local_other" ]; then
        printf '### Other changes\n\n'
        printf '%s\n' "$local_other" \
          | awk '{ print toupper(substr($0, 1, 1)) substr($0, 2) }' \
          | sed -E 's/^/- /'
        printf '\n'
      fi
    fi
  fi

  printf '**Full changelog**: %s\n' "$compare_url"
} > "$output"

cat "$output"
