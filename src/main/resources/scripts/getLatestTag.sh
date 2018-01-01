#!/bin/bash
git tag -l --format="%(creatordate:iso8601)|%(refname:short)" | sort -r | head -n 1 | awk -F'|' '{print $2}'
