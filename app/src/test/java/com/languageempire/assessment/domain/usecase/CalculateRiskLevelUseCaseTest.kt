package com.languageempire.assessment.domain.usecase

import com.languageempire.assessment.domain.model.RiskLevel
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CalculateRiskLevelUseCaseTest {

    private lateinit var useCase: CalculateRiskLevelUseCase

    @Before
    fun setUp() {
        useCase = CalculateRiskLevelUseCase()
    }

    @Test
    fun `returns red when unassigned bookings reach red threshold`() {
        val result = useCase(
            totalRequests = 100,
            availableInterpreters = 25,
            unassignedBookings = 20,
            averageWaitingTimeMinutes = 5
        )

        Assert.assertEquals(RiskLevel.RED, result)
    }

    @Test
    fun `returns red when available interpreters are critically low`() {
        val result = useCase(
            totalRequests = 100,
            availableInterpreters = 10,
            unassignedBookings = 5,
            averageWaitingTimeMinutes = 5
        )

        Assert.assertEquals(RiskLevel.RED, result)
    }

    @Test
    fun `returns red when waiting time reaches red threshold`() {
        val result = useCase(
            totalRequests = 100,
            availableInterpreters = 30,
            unassignedBookings = 5,
            averageWaitingTimeMinutes = 15
        )

        Assert.assertEquals(RiskLevel.RED, result)
    }

    @Test
    fun `returns red when unassigned ratio reaches red threshold`() {
        val result = useCase(
            totalRequests = 100,
            availableInterpreters = 30,
            unassignedBookings = 20,
            averageWaitingTimeMinutes = 5
        )

        Assert.assertEquals(RiskLevel.RED, result)
    }

    @Test
    fun `returns amber when unassigned bookings reach amber threshold`() {
        val result = useCase(
            totalRequests = 100,
            availableInterpreters = 30,
            unassignedBookings = 10,
            averageWaitingTimeMinutes = 5
        )

        Assert.assertEquals(RiskLevel.AMBER, result)
    }

    @Test
    fun `returns amber when waiting time reaches amber threshold`() {
        val result = useCase(
            totalRequests = 100,
            availableInterpreters = 30,
            unassignedBookings = 5,
            averageWaitingTimeMinutes = 8
        )

        Assert.assertEquals(RiskLevel.AMBER, result)
    }

    @Test
    fun `returns green when demand is manageable`() {
        val result = useCase(
            totalRequests = 100,
            availableInterpreters = 30,
            unassignedBookings = 3,
            averageWaitingTimeMinutes = 4
        )

        Assert.assertEquals(RiskLevel.GREEN, result)
    }

    @Test
    fun `returns green when total requests are zero and other values are safe`() {
        val result = useCase(
            totalRequests = 0,
            availableInterpreters = 30,
            unassignedBookings = 0,
            averageWaitingTimeMinutes = 0
        )

        Assert.assertEquals(RiskLevel.GREEN, result)
    }

    @Test
    fun `throws when total requests are negative`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            useCase(
                totalRequests = -1,
                availableInterpreters = 30,
                unassignedBookings = 0,
                averageWaitingTimeMinutes = 0
            )
        }
    }

    @Test
    fun `throws when unassigned bookings are greater than total requests`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            useCase(
                totalRequests = 10,
                availableInterpreters = 30,
                unassignedBookings = 11,
                averageWaitingTimeMinutes = 0
            )
        }
    }
}