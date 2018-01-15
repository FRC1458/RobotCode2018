package org.usfirst.frc.team1458.lib.util.flow

import kotlinx.coroutines.experimental.*
import kotlin.system.measureTimeMillis

fun go(block: suspend () -> Unit): Job = launch(CommonPool) { block() }
operator fun (() -> Unit).unaryPlus(): Job = go { this() }
operator fun (suspend () -> Unit).unaryPlus(): Job = go { this() }

class WaitGroup {
    val jobs = mutableListOf<Job>()
    fun add(job: Job): WaitGroup = this.apply { jobs.add(job) }
    fun add(block: suspend () -> Unit): WaitGroup = this.apply {jobs.add(go{block()})}
    fun wait(): Long = measureTimeMillis { runBlocking<Unit> { jobs.forEach { it.join() } } }
}
