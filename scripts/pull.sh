#!/bin/sh

###SCRIPT FOR CI###
#setting error to false, not beautiful but efficient because of updating/merging
set +e
#getting disy data
hg pull
#getting sf data 
hg pull ssh://sebastiangraf@perfidix.hg.sourceforge.net/hgroot/perfidix/perfidix
hg update 2>/dev/null
hg merge 2>/dev/null
hg commit -m "merged disy and sf data"
exit 0
