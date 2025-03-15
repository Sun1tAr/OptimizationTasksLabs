import numpy as np

class LinearProgramming:
    m = 3
    n = 3
    l = n + m - 1
    flag = True

    source = np.zeros((m, n))
    table = 0

    basis = []

    def stopCriterion(self):
        self.flag = True

    print(source)