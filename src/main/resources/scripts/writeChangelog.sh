#!/bin/bash
$1 -u cflint -p CFLint --token $2 --output CHANGELOG.md --base CHANGELOG.md --no-unreleased --since-tag $3 --header-label
