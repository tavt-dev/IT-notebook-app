package com.tien.it_notebook_app.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tien.it_notebook_app.data.model.Formula;
import com.tien.it_notebook_app.data.model.Topic;
import com.tien.it_notebook_app.utils.Converters;

import java.util.concurrent.Executors;

@Database(entities = {Topic.class, Formula.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract TopicDao topicDao();
    public abstract FormulaDao formulaDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "it_notebook_db"
                    ).addCallback(SEED_CALLBACK).build();
                }
            }
        }
        return INSTANCE;
    }

    /** Chạy một lần duy nhất khi DB được tạo — tương đương data.sql trong Spring Boot */
    private static final RoomDatabase.Callback SEED_CALLBACK = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase database = INSTANCE;
                if (database == null) return;

                TopicDao topicDao = database.topicDao();
                FormulaDao formulaDao = database.formulaDao();

                long now = System.currentTimeMillis();

                // --- Seed Topics ---
                long idAlgo   = topicDao.insert(new Topic("Algorithms",       "ic_algorithm", 0xFF4D7CFF, now));
                long idDs     = topicDao.insert(new Topic("Data Structures",   "ic_algorithm", 0xFF4CAF50, now));
                long idSql    = topicDao.insert(new Topic("SQL Queries",       "ic_algorithm", 0xFFFF9800, now));
                long idRegex  = topicDao.insert(new Topic("Regex",             "ic_algorithm", 0xFF9C27B0, now));

                // --- Seed Formulas: Algorithms ---
                formulaDao.insert(new Formula(
                        "Binary Search",
                        (int) idAlgo,
                        "int binarySearch(int[] arr, int target) {\n" +
                        "    int lo = 0, hi = arr.length - 1;\n" +
                        "    while (lo <= hi) {\n" +
                        "        int mid = lo + (hi - lo) / 2;\n" +
                        "        if (arr[mid] == target) return mid;\n" +
                        "        if (arr[mid] < target) lo = mid + 1;\n" +
                        "        else hi = mid - 1;\n" +
                        "    }\n" +
                        "    return -1;\n" +
                        "}",
                        "Tìm kiếm nhị phân trên mảng đã sắp xếp. Độ phức tạp: O(log n).",
                        "binarySearch(new int[]{1,3,5,7,9}, 5) → 2",
                        new String[]{"algorithms", "search", "O(log n)"},
                        false, now, now, 0
                ));

                formulaDao.insert(new Formula(
                        "Bubble Sort",
                        (int) idAlgo,
                        "void bubbleSort(int[] arr) {\n" +
                        "    int n = arr.length;\n" +
                        "    for (int i = 0; i < n - 1; i++)\n" +
                        "        for (int j = 0; j < n - i - 1; j++)\n" +
                        "            if (arr[j] > arr[j+1]) {\n" +
                        "                int tmp = arr[j];\n" +
                        "                arr[j] = arr[j+1];\n" +
                        "                arr[j+1] = tmp;\n" +
                        "            }\n" +
                        "}",
                        "Sắp xếp nổi bọt. Độ phức tạp: O(n²).",
                        "Mảng {5,3,1,4,2} → {1,2,3,4,5}",
                        new String[]{"algorithms", "sorting", "O(n2)"},
                        false, now, now, 0
                ));

                formulaDao.insert(new Formula(
                        "Dijkstra's Algorithm",
                        (int) idAlgo,
                        "// PriorityQueue<int[]> {dist, node}\n" +
                        "PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));\n" +
                        "pq.offer(new int[]{0, src});\n" +
                        "while (!pq.isEmpty()) {\n" +
                        "    int[] cur = pq.poll();\n" +
                        "    int d = cur[0], u = cur[1];\n" +
                        "    for (int[] edge : graph[u])\n" +
                        "        if (d + edge[1] < dist[edge[0]]) {\n" +
                        "            dist[edge[0]] = d + edge[1];\n" +
                        "            pq.offer(new int[]{dist[edge[0]], edge[0]});\n" +
                        "        }\n" +
                        "}",
                        "Tìm đường đi ngắn nhất trong đồ thị có trọng số không âm. O((V+E) log V).",
                        "Shortest path in weighted graph from source node.",
                        new String[]{"algorithms", "graph", "shortest-path"},
                        true, now, now, now - 3600000
                ));

                // --- Seed Formulas: Data Structures ---
                formulaDao.insert(new Formula(
                        "Stack (push/pop)",
                        (int) idDs,
                        "Deque<Integer> stack = new ArrayDeque<>();\n" +
                        "stack.push(1);   // push\n" +
                        "stack.pop();     // pop\n" +
                        "stack.peek();    // top without remove",
                        "Stack theo nguyên tắc LIFO. Dùng ArrayDeque thay Stack class.",
                        "stack.push(1); stack.push(2); stack.pop() → 2",
                        new String[]{"data-structures", "stack", "LIFO"},
                        false, now, now, 0
                ));

                formulaDao.insert(new Formula(
                        "HashMap Operations",
                        (int) idDs,
                        "Map<String, Integer> map = new HashMap<>();\n" +
                        "map.put(\"key\", 1);\n" +
                        "map.get(\"key\");          // → 1\n" +
                        "map.getOrDefault(\"k\", 0);\n" +
                        "map.containsKey(\"key\");  // → true\n" +
                        "map.remove(\"key\");\n" +
                        "for (var e : map.entrySet())\n" +
                        "    System.out.println(e.getKey() + \":\" + e.getValue());",
                        "Thao tác cơ bản với HashMap. Lookup O(1) trung bình.",
                        "Đếm tần suất: map.put(c, map.getOrDefault(c, 0) + 1)",
                        new String[]{"data-structures", "hashmap", "O(1)"},
                        true, now, now, now - 1800000
                ));

                // --- Seed Formulas: SQL ---
                formulaDao.insert(new Formula(
                        "JOIN Types",
                        (int) idSql,
                        "-- INNER JOIN: chỉ bản ghi khớp cả 2 bảng\n" +
                        "SELECT * FROM orders o\n" +
                        "INNER JOIN customers c ON o.customer_id = c.id;\n\n" +
                        "-- LEFT JOIN: tất cả từ bảng trái\n" +
                        "SELECT * FROM customers c\n" +
                        "LEFT JOIN orders o ON c.id = o.customer_id;",
                        "INNER JOIN: giao. LEFT JOIN: tất cả trái + khớp phải. RIGHT JOIN: ngược lại.",
                        "Tìm khách chưa có đơn: LEFT JOIN + WHERE o.id IS NULL",
                        new String[]{"sql", "join", "database"},
                        false, now, now, 0
                ));

                formulaDao.insert(new Formula(
                        "GROUP BY & HAVING",
                        (int) idSql,
                        "SELECT department, COUNT(*) as total,\n" +
                        "       AVG(salary) as avg_salary\n" +
                        "FROM employees\n" +
                        "GROUP BY department\n" +
                        "HAVING COUNT(*) > 5\n" +
                        "ORDER BY avg_salary DESC;",
                        "GROUP BY nhóm dữ liệu. HAVING lọc sau khi group (khác WHERE lọc trước).",
                        "Phòng ban có hơn 5 nhân viên, sắp theo lương TB giảm dần.",
                        new String[]{"sql", "aggregate", "group-by"},
                        true, now, now, now - 900000
                ));

                // --- Seed Formulas: Regex ---
                formulaDao.insert(new Formula(
                        "Common Regex Patterns",
                        (int) idRegex,
                        "// Email\n" +
                        "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$\n\n" +
                        "// Phone VN (10 số)\n" +
                        "^(0|\\+84)[3-9][0-9]{8}$\n\n" +
                        "// URL\n" +
                        "https?://[\\w-]+(\\.[\\w-]+)+(/[\\S]*)?\n\n" +
                        "// Số nguyên dương\n" +
                        "^[1-9][0-9]*$",
                        "Các pattern Regex phổ biến cho validate đầu vào.",
                        "email.matches(\"^[\\\\w.-]+@[\\\\w.-]+\\\\.[a-zA-Z]{2,}$\")",
                        new String[]{"regex", "validation", "pattern"},
                        false, now, now, 0
                ));
            });
        }
    };
}
