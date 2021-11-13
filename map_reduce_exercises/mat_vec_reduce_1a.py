import os
import sys
import re

import mapper_reducer


def reduce_it(emit, key, vals):
    multi = float(vals[0].split()[2])
    sum = 0
    
    for i in range(1, len(vals)):
        sum += (multi * float(vals[i].split()[2]))
        
    emit(key, sum)


mr = mapper_reducer.MapperReducer()
mr.reduce(reduce_it, sys.stdin)


