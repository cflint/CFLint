#!/bin/bash
/Users/kai/.rbenv/shims/github_changelog_generator -u cflint -p CFLint --token <github token> --output CHANGELOG.md --base CHANGELOG.md --no-unreleased --since-tag $1 --header-label
