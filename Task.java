import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String description;
    private String status; // "todo", "in-progress", "done"
    private String createdAt;
    private String updatedAt;

    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.status = "todo";
        this.createdAt = now();
        this.updatedAt = this.createdAt;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = now();
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = now();
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    // Convert task to JSON line manually (no libraries)
    public String toJson() {
        return String.format(
                "{\"id\":%d,\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updatedAt\":\"%s\"}",
                id, escape(description), status, createdAt, updatedAt
        );
    }

    // Basic JSON escaping
    private String escape(String text) {
        return text.replace("\"", "\\\"");
    }

    @Override
    public String toString() {
        return String.format(
                "%-3d | %-12s | %s (created: %s, updated: %s)",
                id, status, description, createdAt, updatedAt
        );
    }
}
