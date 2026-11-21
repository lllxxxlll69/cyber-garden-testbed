package garden.training

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class NotesController(private val jdbcTemplate: JdbcTemplate) {
    @GetMapping("/health")
    fun health() = mapOf("status" to "training-only")

    @GetMapping("/notes")
    fun notes(@RequestParam(defaultValue = "demo") owner: String): List<Map<String, Any>> {
        return jdbcTemplate.queryForList("SELECT id, owner, body FROM notes WHERE owner = '" + owner + "'")
    }
}
