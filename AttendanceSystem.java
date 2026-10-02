import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Student {
    protected String nisn;
    protected String name;
    protected String nis;
    protected String studentClass;

    public Student(String nisn, String name, String nis, String studentClass) {
        this.nisn = nisn;
        this.name = name;
        this.nis = nis;
        this.studentClass = studentClass;
    }

    public void displayInfo() {
        System.out.println("NISN        : " + nisn);
        System.out.println("Nama        : " + name);
        System.out.println("NIS         : " + nis);
        System.out.println("Kelas       : " + studentClass);
    }
}

class Attendance extends Student {
    private final String status;

    public Attendance(Student student, String status) {
        super(student.nisn, student.name, student.nis, student.studentClass);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

 @Override 
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Keterangan  : " + status);
        System.out.println("-----------------------------------");
    }
}

public class AttendanceSystem {
    
    private static final String DATA = """
        [
            {"nisn": "0098946020", "name": "Muhammad Khabib Zadit Taqwa", "nis": "7541", "class": "XI RPL 4"},
            {"nisn": "0109106422", "name": "Ni Ketut Nadya Julia Pradnyani", "nis": "7542", "class": "XI RPL 4"},
            {"nisn": "0107486760", "name": "Ni Putu Nilam Pranita Putri", "nis": "7543", "class": "XI RPL 4"},
            {"nisn": "0109463239", "name": "Nyoman Dharmantra", "nis": "7545", "class": "XI RPL 4"},
            {"nisn": "0087756734", "name": "Putu Eka Agus Setiawan", "nis": "7546", "class": "XI RPL 4"}
        ]
        """;

    static List<Student> loadStudentsFromJSON() {
        List<Student> studentList = new ArrayList<>();
        String regex = "\\{\\s*\"nisn\":\\s*\"([^\"]+)\",\\s*\"name\":\\s*\"([^\"]+)\",\\s*\"nis\":\\s*\"([^\"]+)\",\\s*\"class\":\\s*\"([^\"]+)\"\\s*\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(DATA);

        while (matcher.find()) {
            String nisn = matcher.group(1);
            String name = matcher.group(2);
            String nis = matcher.group(3);
            String studentClass = matcher.group(4);
            studentList.add(new Student(nisn, name, nis, studentClass));
        }
        return studentList;
    }

    public static void main(String[] args) {
        List<Student> studentDatabase = loadStudentsFromJSON();
        List<Attendance> attendanceList = new ArrayList<>();
        int choice;

        try (Scanner scanner = new Scanner(System.in)) {
            do {
                System.out.println("\n=== APLIKASI ABSENSI SISWA ===");
                System.out.println("1. Kirim Absen (Masukkan NIS)");
                System.out.println("2. Tampilkan Rekap Absen");
                System.out.println("3. Keluar");
                System.out.print("Pilih menu (1-3): ");

                while (!scanner.hasNextInt()) {
                    System.out.print("Masukkan angka yang valid: ");
                    scanner.next();
                }
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.print("\nMasukkan NIS Siswa: ");
                        String inputNis = scanner.nextLine();

                        Student foundStudent = null;
                        for (Student s : studentDatabase) {
                            if (s.nis.equals(inputNis)) {
                                foundStudent = s;
                                break;
                            }
                        }

                        if (foundStudent != null) {
                            System.out.println("Siswa Ditemukan: " + foundStudent.name + " (" + foundStudent.studentClass + ")");
                            System.out.print("Masukkan Keterangan (Hadir/Sakit/Izin/Alpa): ");
                            String status = scanner.nextLine();

                            Attendance newAttendance = new Attendance(foundStudent, status);
                            attendanceList.add(newAttendance);
                            System.out.println("Absensi berhasil dicatat!");
                        } else {
                            System.out.println("Siswa dengan NIS " + inputNis + " tidak ditemukan dalam data!");
                        }
                    }

                    case 2 -> {
                        System.out.println("\n===================================");
                        System.out.println("       ABSENSI SISWA");
                        System.out.println("===================================");
                        if (attendanceList.isEmpty()) {
                            System.out.println("Belum ada data absensi.");
                        } else {
                            int index = 1;
                            for (Attendance record : attendanceList) {
                                System.out.println("Data ke " + index++);
                                record.displayInfo();
                            }
                        }
                    }

                    case 3 -> System.out.println("Terima kasih telah menggunakan sistem absensi!");

                    default -> System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                }
            } while (choice != 3);
        }
    }
}