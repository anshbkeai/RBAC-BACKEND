import math

def print_board(board):
    for row in board:
        print(" | ".join(row))
        print("-" * 9)

def is_moves_left(board):
    for row in board:
        if ' ' in row:
            return True
    return False

def evaluate(board):
    # Check rows and columns
    for i in range(3):
        if board[i][0] == board[i][1] == board[i][2]:
            if board[i][0] == 'X':
                return 10
            elif board[i][0] == 'O':
                return -10
        if board[0][i] == board[1][i] == board[2][i]:
            if board[0][i] == 'X':
                return 10
            elif board[0][i] == 'O':
                return -10
    # Check diagonals
    if board[0][0] == board[1][1] == board[2][2]:
        if board[0][0] == 'X':
            return 10
        elif board[0][0] == 'O':
            return -10
    if board[0][2] == board[1][1] == board[2][0]:
        if board[0][2] == 'X':
            return 10
        elif board[0][2] == 'O':
            return -10
    return 0

def minimax(board, depth, is_max):
    score = evaluate(board)
    if score == 10 or score == -10:
        return score
    if not is_moves_left(board):
        return 0

    if is_max:
        best = -math.inf
        for i in range(3):
            for j in range(3):
                if board[i][j] == ' ':
                    board[i][j] = 'X'
                    best = max(best, minimax(board, depth + 1, not is_max))
                    board[i][j] = ' '
        return best
    else:
        best = math.inf
        for i in range(3):
            for j in range(3):
                if board[i][j] == ' ':
                    board[i][j] = 'O'
                    best = min(best, minimax(board, depth + 1, not is_max))
                    board[i][j] = ' '
        return best

def find_best_move(board):
    best_val = -math.inf
    best_move = (-1, -1)
    for i in range(3):
        for j in range(3):
            if board[i][j] == ' ':
                board[i][j] = 'X'
                move_val = minimax(board, 0, False)
                board[i][j] = ' '
                if move_val > best_val:
                    best_move = (i, j)
                    best_val = move_val
    return best_move

def main():
    board = [[' ' for _ in range(3)] for _ in range(3)]
    print("Tic Tac Toe: You are O, AI is X")
    while True:
        print_board(board)
        if not is_moves_left(board) or abs(evaluate(board)) == 10:
            break
        # Player move
        while True:
            try:
                row = int(input("Enter row (0-2): "))
                col = int(input("Enter col (0-2): "))
                if board[row][col] == ' ':
                    board[row][col] = 'O'
                    break
                else:
                    print("Cell already taken.")
            except:
                print("Invalid input.")
        if not is_moves_left(board) or abs(evaluate(board)) == 10:
            break
        # AI move
        move = find_best_move(board)
        if move != (-1, -1):
            board[move[0]][move[1]] = 'X'
    print_board(board)
    score = evaluate(board)
    if score == 10:
        print("AI wins!")
    elif score == -10:
        print("You win!")
    else:
        print("Draw!")

if __name__ == "__main__":
    main()