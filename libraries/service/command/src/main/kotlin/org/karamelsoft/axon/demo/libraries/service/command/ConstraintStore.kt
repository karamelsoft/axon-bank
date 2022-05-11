package org.karamelsoft.axon.demo.libraries.service.command

import org.karamelsoft.research.axon.libraries.service.api.Status
import java.time.Duration

interface ConstraintStore {
    fun claimConstraint(id: String, duration: Duration): Status<Unit>
    fun validateConstraint(id: String): Status<Unit>
    fun releaseConstraint(id: String): Status<Unit>
}
