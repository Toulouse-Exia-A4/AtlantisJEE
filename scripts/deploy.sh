#!/bin/bash
sshpass -p '$SSH_PASS' ssh -o "StrictHostKeyChecking no" $SSH_USER@$SSH_ENDPOINT -p $SSH_PORT pkill java