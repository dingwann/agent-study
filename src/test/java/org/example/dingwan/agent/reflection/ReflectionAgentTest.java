package org.example.dingwan.agent.reflection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReflectionAgentTest {

    @Autowired ReflectionAgent reflectionAgent;

    @Test
    void test() {
        String run = reflectionAgent.run("编写一个Python函数，找出1到n之间所有的素数 (prime numbers)。");
        /**
         * --- 开始处理任务 ---
         * 任务：编写一个Python函数，找出1到n之间所有的素数 (prime numbers)。
         * --- 正在进行初始尝试 ---
         * --- 初始代码 ---
         * ```python
         * from typing import List
         *
         * def primes_up_to(n: int) -> List[int]:
         *     """
         *     Return a list of all prime numbers between 1 and n (inclusive).
         *
         *     Args:
         *         n (int): The upper bound of the range (must be an integer).
         *
         *     Returns:
         *         List[int]: A list of prime numbers less than or equal to n.
         *     """
         *     if n < 2:
         *         return []
         *     is_prime = [True] * (n + 1)
         *     is_prime[0] = is_prime[1] = False
         *     for i in range(2, int(n**0.5) + 1):
         *         if is_prime[i]:
         *             for j in range(i * i, n + 1, i):
         *                 is_prime[j] = False
         *     primes = [i for i, prime in enumerate(is_prime) if prime]
         *     return primes
         * ```
         * 📝 记忆已更新，新增一条execution记录
         * --- 第 1/5 轮迭代
         * -> 正在进行反思
         * 📝 记忆已更新，新增一条reflection记录
         * ✅ 反思认为代码已无需改进，任务完成。
         * --- 任务完成 ---
         * 最终生成的代码:
         * ```python
         * from typing import List
         *
         * def primes_up_to(n: int) -> List[int]:
         *     """
         *     Return a list of all prime numbers between 1 and n (inclusive).
         *
         *     Args:
         *         n (int): The upper bound of the range (must be an integer).
         *
         *     Returns:
         *         List[int]: A list of prime numbers less than or equal to n.
         *     """
         *     if n < 2:
         *         return []
         *     is_prime = [True] * (n + 1)
         *     is_prime[0] = is_prime[1] = False
         *     for i in range(2, int(n**0.5) + 1):
         *         if is_prime[i]:
         *             for j in range(i * i, n + 1, i):
         *                 is_prime[j] = False
         *     primes = [i for i, prime in enumerate(is_prime) if prime]
         *     return primes
         * ```
         */
    }

}