#!/bin/bash

curl --verbose localhost:8500/operations/health | python -m json.tool

