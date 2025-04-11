package Console;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ToDoListApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TaskManager taskManager = new TaskManager();
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static void main(String[] args) {
        while (true) {
            printMenu();
            handleUserChoice();
        }
    }

    private static void printMenu() {
        System.out.println("\n=== To-Do List Manager ===");
        System.out.println("1. Показать все задачи");
        System.out.println("2. Показать выполненные задачи");
        System.out.println("3. Показать невыполненные задачи");
        System.out.println("4. Задачи по дате создания");
        System.out.println("5. Добавить новую задачу");
        System.out.println("6. Удалить задачу");
        System.out.println("7. Отметить как выполненную");
        System.out.println("8. Выход");
        System.out.print("Выберите действие: ");
    }

    private static void handleUserChoice() {
        int choice = readIntInput();
        switch (choice) {
            case 1 -> showAllTasks();
            case 2 -> showCompletedTasks();
            case 3 -> showIncompleteTasks();
            case 4 -> showTasksByDate();
            case 5 -> addNewTask();
            case 6 -> deleteTask();
            case 7 -> markTaskAsCompleted();
            case 8 -> exitProgram();
            default -> System.out.println("Неверный выбор!");
        }
    }

    private static void showAllTasks() {
        printTasks(taskManager.getAllTasks());
    }

    private static void showCompletedTasks() {
        printTasks(taskManager.filterByStatus(true));
    }

    private static void showIncompleteTasks() {
        printTasks(taskManager.filterByStatus(false));
    }

    private static void showTasksByDate() {
        System.out.print("Введите дату (ГГГГ-ММ-ДД): ");
        try {
            LocalDate date = LocalDate.parse(scanner.nextLine());
            printTasks(taskManager.filterByDate(date));
        } catch (DateTimeParseException e) {
            System.out.println("Некорректный формат даты!");
        }
    }

    private static void addNewTask() {
        System.out.print("Введите описание задачи: ");
        String description = scanner.nextLine();
        try {
            taskManager.addTask(description);
            System.out.println("Задача успешно добавлена!");
        } catch (InvalidInputException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void deleteTask() {
        int index = readIntInput("Введите индекс задачи для удаления: ");
        try {
            taskManager.deleteTask(index);
            System.out.println("Задача успешно удалена!");
        } catch (TaskNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void markTaskAsCompleted() {
        int index = readIntInput("Введите индекс задачи для отметки: ");
        try {
            taskManager.markTaskCompleted(index);
            System.out.println("Задача отмечена как выполненная!");
        } catch (TaskNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void printTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Задачи не найдены");
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.printf("%d. [%s] %s%nСоздано: %s%n",
                    i,
                    task.isCompleted() ? "X" : " ",
                    task.getDescription(),
                    task.getCreatedDate().format(DATE_FORMATTER));
            if (task.isCompleted()) {
                System.out.println("Выполнено: " + 
                    task.getCompletedDate().format(DATE_FORMATTER));
            }
            System.out.println("-------------------");
        }
    }

    private static int readIntInput() {
        return readIntInput("");
    }

    private static int readIntInput(String prompt) {
        while (true) {
            try {
                if (!prompt.isEmpty()) System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введите целое число!");
            }
        }
    }

    private static void exitProgram() {
        System.out.println("Работа программы завершена");
        System.exit(0);
    }
}