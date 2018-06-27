#!/bin/bash
sshpass -p '$SSH_PASS' ssh $SSH_USER@$SSH_ENDPOINT -p $SSH_PORT touch hey