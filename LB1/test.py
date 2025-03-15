def simplex_method(c, A, b):
    """
    Реализует симплекс-метод для решения задачи линейного программирования.

    Args:
        c: Вектор коэффициентов целевой функции (максимизация).
        A: Матрица коэффициентов ограничений (A * x <= b).
        b: Вектор свободных членов ограничений.

    Returns:
        Результат: (optimal_value, optimal_solution, status)
            optimal_value: Оптимальное значение целевой функции.
            optimal_solution: Оптимальное решение (вектор x).
            status: Строка, указывающая на результат ('Optimal', 'Unbounded', 'Infeasible').
    """

    m, n = len(b), len(c)
    tableau = create_tableau(c, A, b)  # Создаем симплекс-таблицу

    basic_vars = list(range(n, n + m)) # Индексы искусственных переменных

    # Основной цикл симплекс-метода
    while True:
        entering_var_index = find_entering_variable(tableau)
        if entering_var_index is None:
            # Найден оптимальный базис
            return extract_solution(tableau, basic_vars, n, m)

        leaving_var_index = find_leaving_variable(tableau, entering_var_index)
        if leaving_var_index is None:
            # Задача не ограничена сверху
            return None, None, 'Unbounded'

        pivot(tableau, leaving_var_index, entering_var_index)
        basic_vars[leaving_var_index] = entering_var_index  # Обновляем базисные переменные


def create_tableau(c, A, b):
    """
    Создает симплекс-таблицу из исходных данных.

    Args:
        c: Вектор коэффициентов целевой функции.
        A: Матрица коэффициентов ограничений.
        b: Вектор свободных членов ограничений.

    Returns:
        Симплекс-таблица (двумерный список).
    """
    m, n = len(b), len(c)
    tableau = []

    # Добавляем матрицу A и вектор b
    for i in range(m):
        tableau.append(A[i] + [b[i]])

    # Добавляем строку целевой функции (с противоположным знаком)
    objective_row = [-val for val in c] + [0]
    tableau.append(objective_row)  # Целевая функция идет последней строкой

    return tableau


def find_entering_variable(tableau):
    """
    Находит индекс входящей переменной (переменной, которая войдет в базис).
    Использует правило Бленда (выбирает переменную с наименьшим индексом при равенстве).

    Args:
        tableau: Симплекс-таблица.

    Returns:
        Индекс входящей переменной или None, если оптимальное решение найдено.
    """
    objective_row = tableau[-1]
    for i, val in enumerate(objective_row[:-1]):
        if val > 1e-6:  # Положительное значение (с допуском на ошибки округления)
            return i
    return None


def find_leaving_variable(tableau, entering_var_index):
    """
    Находит индекс выходящей переменной (переменной, которая покинет базис).

    Args:
        tableau: Симплекс-таблица.
        entering_var_index: Индекс входящей переменной.

    Returns:
        Индекс выходящей переменной или None, если задача не ограничена сверху.
    """
    min_ratio = float('inf')
    leaving_var_index = None
    m = len(tableau) - 1  # Исключаем строку целевой функции

    for i in range(m):
        a_ij = tableau[i][entering_var_index]
        if a_ij > 1e-6: # Положительное значение
            ratio = tableau[i][-1] / a_ij  # b_i / a_ij
            if ratio < min_ratio:
                min_ratio = ratio
                leaving_var_index = i

    return leaving_var_index


def pivot(tableau, leaving_var_index, entering_var_index):
    """
    Выполняет операцию приведения (pivot) симплекс-таблицы.

    Args:
        tableau: Симплекс-таблица.
        leaving_var_index: Индекс выходящей переменной (строка).
        entering_var_index: Индекс входящей переменной (столбец).
    """

    pivot_element = tableau[leaving_var_index][entering_var_index]

    # Нормализуем строку пивота
    for j in range(len(tableau[0])):
        tableau[leaving_var_index][j] /= pivot_element

    # Обновляем остальные строки
    for i in range(len(tableau)):
        if i != leaving_var_index:
            factor = tableau[i][entering_var_index]
            for j in range(len(tableau[0])):
                tableau[i][j] -= factor * tableau[leaving_var_index][j]


def extract_solution(tableau, basic_vars, n, m):
    """
    Извлекает оптимальное решение и значение целевой функции из симплекс-таблицы.

    Args:
        tableau: Симплекс-таблица.
        basic_vars: Список индексов базисных переменных.
        n: Количество исходных переменных.
        m: Количество ограничений.

    Returns:
        (optimal_value, optimal_solution, status)
    """
    optimal_solution = [0] * n
    for i, var_index in enumerate(basic_vars):
        if var_index < n:
            optimal_solution[var_index] = tableau[i][-1]

    optimal_value = -tableau[-1][-1]  # Изменяем знак, т.к. мы минимизировали -z
    return optimal_value, optimal_solution, 'Optimal'


# Пример использования:
if __name__ == '__main__':
    # Пример задачи:
    # Максимизировать: z = 3x1 + 2x2
    # При ограничениях:
    # x1 + x2 <= 4
    # 2x1 + x2 <= 5
    # x1, x2 >= 0

    c = [3, 2, 4]  # Коэффициенты целевой функции
    A = [[1, 1, 4], [2, 1, 1]]  # Коэффициенты ограничений
    b = [4, 5, 1]  # Свободные члены ограничений

    # Решаем задачу
    optimal_value, optimal_solution, status = simplex_method(c, A, b)



    # Пример задачи с искусственными переменными:
    # Минимизировать: z = x1 + x2
    # При ограничениях:
    # 2x1 + x2 >= 4
    # x1 + 2x2 >= 5
    # x1, x2 >= 0

    # Преобразуем задачу к максимизации и типу "меньше или равно":
    c_artificial = [-1, -1]
    A_artificial = [[-2, -1], [-1, -2]]
    b_artificial = [-4, -5]

    # Добавим искусственные переменные (с большими штрафами M, для простоты просто число)
    # На самом деле M = 100 должно быть достаточно большим, чтобы искусственные переменные покинули базис
    A_artificial = [[-2, -1, 1, 0], [-1, -2, 0, 1]]
    c_artificial = [-1, -1, -100, -100] #Минимизация, т.е. добавляем -Mz в целевую функцию
    b_artificial = [-4, -5]

    optimal_value_artificial, optimal_solution_artificial, status_artificial = simplex_method(c_artificial, A_artificial, b_artificial)

    print("\nЗадача с искусственными переменными:")
    print("Статус:", status_artificial)
    if status_artificial == 'Optimal':
        print("Оптимальное значение целевой функции:", optimal_value_artificial)
        print("Оптимальное решение:", optimal_solution_artificial[:2])  # Берем только первые две переменные (x1 и x2)
    elif status_artificial == 'Unbounded':
        print("Задача не ограничена сверху.")
    elif status_artificial == 'Infeasible':
        print("Задача не имеет допустимых решений.")