package Console;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private List<Task> tasks;
    private static final String FILE_PATH = "tasks.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public TaskManager() {
        tasks = new ArrayList<>();
        loadFromFile();
    }

    public void addTask(String description) throws InvalidInputException {
        validateDescription(description);
        tasks.add(new Task(description));
        saveToFile();
    }

    public void deleteTask(int index) throws TaskNotFoundException {
        validateIndex(index);
        tasks.remove(index);
        saveToFile();
    }

    public void markTaskCompleted(int index) throws TaskNotFoundException {
        validateIndex(index);
        Task task = tasks.get(index);
        task.setCompleted(true);
        saveToFile();
    }

    public List<Task> filterByStatus(boolean isCompleted) {
        return tasks.stream()
                .filter(task -> task.isCompleted() == isCompleted)
                .collect(Collectors.toList());
    }

    public List<Task> filterByDate(LocalDate date) {
        return tasks.stream()
                .filter(task -> task.getCreatedDate().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Task task : tasks) {
                String line = String.join("|",
                        task.getDescription(),
                        String.valueOf(task.isCompleted()),
                        task.getCreatedDate().format(formatter),
                        task.getCompletedDate() != null ? 
                            task.getCompletedDate().format(formatter) : "null"
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка сохранения файла: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                Task task = new Task(parts[0]);
                task.setCompleted(Boolean.parseBoolean(parts[1]));
                task.setCreatedDate(LocalDateTime.parse(parts[2], formatter));
                if (!"null".equals(parts[3])) {
                    task.setCompletedDate(LocalDateTime.parse(parts[3], formatter));
                }
                tasks.add(task);
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    private void validateIndex(int index) throws TaskNotFoundException {
        if (index < 0 || index >= tasks.size()) {
            throw new TaskNotFoundException("Задача с индексом " + index + " не найдена");
        }
    }

    private void validateDescription(String description) throws InvalidInputException {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidInputException("Описание задачи не может быть пустым");
        }
        if (description.contains("|")) {
            throw new InvalidInputException("Запрещённый символ '|' в описании");
        }
    }
}