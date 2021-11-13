import os
import sys
import re

import mapper_reducer


def map_it(emit, file_handle):
    for line in file_handle:
        info = line.split()
        
        student = info[0]
        weight = info[1]
        grade = info[2]
        
        emit(student, float(weight) * float(grade))

path = './data/gradebook'
mr = mapper_reducer.MapperReducer()
mr.map(map_it, path)
