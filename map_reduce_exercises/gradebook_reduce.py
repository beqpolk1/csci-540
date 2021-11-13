import os
import sys
import re

import mapper_reducer


def reduce_it(emit, key, vals):
    vals_as_floats = map(float, vals)
    emit(key, sum(vals_as_floats))


mr = mapper_reducer.MapperReducer()
mr.reduce(reduce_it, sys.stdin)


