import heapq


goal_state = [
    [1, 2, 3],
    [4, 5, 6],
    [7, 8, 0]
]

moves = [(-1,0),(1,0),(0,-1),(0,1)]

def manhattan(puzzle):
    """Heuristic: Manhattan Distance"""
    distance = 0
    for i in range(3):
        for j in range(3):
            val = puzzle[i][j]
            if val != 0:
                target_x = (val - 1) // 3
                target_y = (val - 1) % 3
                distance += abs(i - target_x) + abs(j - target_y)
    return distance

def is_valid(x, y):
    return 0 <= x < 3 and 0 <= y < 3

def get_neighbors(state):
    neighbors = []
    for i in range(3):
        for j in range(3):
            if state[i][j] == 0:
                x, y = i, j
                break
    for dx, dy in moves:
        nx, ny = x + dx, y + dy
        if is_valid(nx, ny):
            new_state = [row[:] for row in state]
            new_state[x][y], new_state[nx][ny] = new_state[nx][ny], new_state[x][y]
            neighbors.append(new_state)
    return neighbors

def serialize(state):
    return tuple(tuple(row) for row in state)

def print_path(path):
    for step in path:
        for row in step:
            print(row)
        print("↓")

def a_star(start):
    open_set = []
    heapq.heappush(open_set, (manhattan(start), 0, start, [start]))  # (f(n), g(n), state, path)
    visited = set()

    while open_set:
        f, g, current, path = heapq.heappop(open_set)
        if current == goal_state:
            print(" Solution Found in", g, "moves.")
            print_path(path)
            return
        serialized = serialize(current)
        if serialized in visited:
            continue
        visited.add(serialized)
        for neighbor in get_neighbors(current):
            if serialize(neighbor) not in visited:
                heapq.heappush(open_set, (g + 1 + manhattan(neighbor), g + 1, neighbor, path + [neighbor]))
    
    print("no soluiton ")

# Example start state
start_state = [
    [1, 2, 3],
    [4, 0, 6],
    [7, 5, 8]
]

a_star(start_state)
