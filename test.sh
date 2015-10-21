#!/usr/bin/env bash

while true;
do
curl 'http://127.0.0.1:8888'
curl 'http://127.0.0.1:8888/stop'
sleep 0.3
done
