#!/bin/bash

# Check if current directory is a Git repository
if ! git rev-parse --is-inside-work-tree &>/dev/null; then
  echo "Error: Not a Git repository. Run script inside a Git repository."
  exit 1
fi

# Get current commit hash
current_commit=$(git rev-parse HEAD)

# Get commit hash of previous version
previous_commit=$(git rev-parse HEAD~1)

# Revert to previous version
git reset --hard $previous_commit
git push origin self-host --force

echo "Reverted to previous version."
