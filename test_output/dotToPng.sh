#!/bin/bash

SCRIPTPATH=${BASH_SOURCE[0]}
SCRIPTDIR=`dirname ${SCRIPTPATH}`

ls ${SCRIPTDIR}/*.dot | while read line
do
	fn="${line%.*}"
	dot -Tpng "${SCRIPTDIR}/$fn.dot" -o "${SCRIPTDIR}/$fn.png"
done 