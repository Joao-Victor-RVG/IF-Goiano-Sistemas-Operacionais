import random

swap = [[i, i + 1, random.randint(1, 50), 0, 0, random.randint(100, 9999)] for i in range(100)]


ram_indices = random.sample(range(100), 10)
ram = [swap[i][:] for i in ram_indices]


def print_matrix(name, matrix):
    print(f"\n{name}:")
    for row in matrix:
        print(row)


def fifo_sc():
    queue = list(range(10))  
    
    for _ in range(1000):
        instrucao = random.randint(1, 100)
        

        page_found = False
        for page in ram:
            if page[1] == instrucao:
                page[3] = 1  
                if random.random() < 0.5:
                    page[2] += 1  
                    page[4] = 1  
                page_found = True
                break
        
        if not page_found:  
                idx = queue.pop(0)  
                if ram[idx][3] == 0:  
                    swap_page = random.choice(swap)
                    
                    if ram[idx][4] == 1: 
                        swap[ram[idx][0]] = ram[idx][:]
                        swap[ram[idx][0]][4] = 0  
                    
                    ram[idx] = swap_page[:]
                    queue.append(idx)
                    break
                else:
                    ram[idx][3] = 0  
                    queue.append(idx)


def ws_clock():
    clock_hand = 0
    
    for _ in range(1000):
        instrucao = random.randint(1, 100)
        

        page_found = False
        for page in ram:
            if page[1] == instrucao:
                page[3] = 1  
                if random.random() < 0.5:
                    page[2] += 1  
                    page[4] = 1  
                page_found = True
                break
        
        if not page_found:  
            while True:
                ep = random.randint(100, 9999)
                page = ram[clock_hand]
                
                if page[3] == 0 and (ep > page[5]):  
                    swap_page = random.choice(swap)
                    
                    if page[4] == 1:  
                        swap[page[0]] = page[:]
                        swap[page[0]][4] = 0  
                    
                    ram[clock_hand] = swap_page[:]
                    break
                
                page[3] = 0  
                clock_hand = (clock_hand + 1) % 10  


print_matrix("Matriz SWAP Inicial", swap)
print_matrix("Matriz RAM Inicial", ram)


fifo_sc()
ws_clock()


print_matrix("Matriz SWAP Final", swap)
print_matrix("Matriz RAM Final", ram)
