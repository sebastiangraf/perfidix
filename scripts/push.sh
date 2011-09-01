#!/bin/sh

###SCRIPT FOR CI###
#setting error to false, not beautiful but efficient because of updating/merging
set +e
#submitting to sourceforge
hg push ssh://sebastiangraf@perfidix.hg.sourceforge.net/hgroot/perfidix/perfidix
exit 0
