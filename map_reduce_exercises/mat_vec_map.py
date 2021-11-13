import os
import sys
import re

import mapper_reducer


def map_it(emit, file_handle):
    for line in file_handle:
        data = line.split()
        
        if len(data) == 2:
            emit(data[0], '0 0 ' + data[1])
        else:
            emit(data[0], data[0] + ' ' + data[1] + ' ' + data[2])

path = './data/mat-vec'
mr = mapper_reducer.MapperReducer()
mr.map(map_it, path)
