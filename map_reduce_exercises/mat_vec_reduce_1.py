import os
import sys
import re

import mapper_reducer


def reduce_it(emit, key, vals):
    multi = float(vals[0].split()[2])
    
    for i in range(1, len(vals)):
        emit(key, float(vals[i].split()[2]) * multi)


mr = mapper_reducer.MapperReducer()
mr.reduce(reduce_it, sys.stdin)


