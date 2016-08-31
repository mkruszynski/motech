#!/bin/bash

body='{
"request": {
  "branch":"master"
}}'

curl -s -X POST \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -H "Travis-API-Version: 3" \
  -H "Authorization: token 6aBhjLXTPBX3e_oszyrNzw" \
  -d "$body" \
  https://api.travis-ci.org/repo/mkruszynski%2Fmotech/requests