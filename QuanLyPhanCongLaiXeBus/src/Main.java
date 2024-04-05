import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<LaiXe> danhSachLaiXe = new ArrayList<>();
    private static final List<Tuyen> danhSachTuyen = new ArrayList<>();
    private static final List<BangPhanCong> bangPhanCong = new ArrayList<>();

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Nhập và lưu danh sách lái xe");
            System.out.println("2. Nhập và lưu danh sách tuyến");
            System.out.println("3. Nhập danh sách phân công lái xe");
            System.out.println("4. Sắp xếp danh sách phân công");
            System.out.println("5. Lập bảng kê tổng khoảng cách chạy xe trong ngày của mỗi lái xe");
            System.out.println("6. Thoát");
            System.out.print("Nhập lựa chọn của bạn: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    nhapVaLuuDanhSachLaiXe();
                    break;
                case 2:
                    nhapVaLuuDanhSachTuyen();
                    break;
                case 3:
                    nhapDanhSachPhanCong();
                    break;
                case 4:
                    sapXepDanhSachPhanCong();
                    break;
                case 5:
                    bangKeKhoangCach();
                    break;
                case 6:
                    exit = true;
                    System.out.println("Đã thoát chương trình.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    break;
            }
        }
    }

    private static void nhapVaLuuDanhSachLaiXe() {
        System.out.println("Nhập số lượng lái xe mới:");
        int n = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.println("Nhập thông tin cho lái xe thứ " + (i + 1) + ":");
            System.out.print("Họ và tên: ");
            String hoTen = scanner.nextLine();
            System.out.print("Địa chỉ: ");
            String diaChi = scanner.nextLine();
            System.out.print("Số điện thoại: ");
            String soDT = scanner.nextLine();
            System.out.print("Trình độ (A - F): ");
            char trinhDo = scanner.nextLine().charAt(0);

            danhSachLaiXe.add(new LaiXe(hoTen, diaChi, soDT, trinhDo));
        }

        luuDanhSachLaiXeVaoFile();
    }

    private static void luuDanhSachLaiXeVaoFile() {
        try (PrintWriter writer = new PrintWriter("danhsachlaixe.txt")) {
            for (LaiXe laiXe : danhSachLaiXe) {
                writer.println(laiXe.getMaLX() + "," + laiXe.getHoTen() + "," + laiXe.getDiaChi() + "," + laiXe.getSoDT() + "," + laiXe.getTrinhDo());
            }
            System.out.println("Đã lưu danh sách lái xe vào file danhsachlaixe.txt.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void nhapVaLuuDanhSachTuyen() {
        System.out.println("Nhập số lượng tuyến mới:");
        int n = scanner.nextInt();
        scanner.nextLine(); // Đọc kí tự '\n'

        for (int i = 0; i < n; i++) {
            System.out.println("Nhập thông tin cho tuyến thứ " + (i + 1) + ":");
            System.out.print("Khoảng cách (km): ");
            double khoangCach = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Số điểm dừng: ");
            int soDiemDung = scanner.nextInt();
            scanner.nextLine();
            danhSachTuyen.add(new Tuyen(khoangCach, soDiemDung));
        }

        luuDanhSachTuyenVaoFile();
    }

    private static void luuDanhSachTuyenVaoFile() {
        try (PrintWriter writer = new PrintWriter("danhsachtuyen.txt")) {
            for (Tuyen tuyen : danhSachTuyen) {
                writer.println(tuyen.getMaTuyen() + "," + tuyen.getKhoangCach() + "," + tuyen.getSoDiemDung());
            }
            System.out.println("Đã lưu danh sách tuyến vào file danhsachtuyen.txt.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void nhapDanhSachPhanCong() {
        if (danhSachLaiXe.isEmpty() || danhSachTuyen.isEmpty()) {
            System.out.println("Danh sách lái xe hoặc danh sách tuyến chưa được nhập. Vui lòng nhập lại.");
            return;
        }

        System.out.println("Nhập số lượng phân công:");
        int n = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.println("Nhập thông tin phân công thứ " + (i + 1) + ":");
            System.out.print("Nhập mã số lái xe: ");
            int maLX = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Nhập mã số tuyến: ");
            int maTuyen = scanner.nextInt();
            scanner.nextLine();

            LaiXe laiXe = timLaiXeTheoMa(maLX);
            Tuyen tuyen = timTuyenTheoMa(maTuyen);

            if (laiXe == null || tuyen == null) {
                System.out.println("Không tìm thấy lái xe hoặc tuyến với mã số tương ứng. Vui lòng kiểm tra lại.");
                continue;
            }

            System.out.print("Nhập số lượt lái: ");
            int soLuot = scanner.nextInt();
            scanner.nextLine();

            bangPhanCong.add(new BangPhanCong(laiXe, tuyen, soLuot));
        }
    }

    private static LaiXe timLaiXeTheoMa(int maLX) {
        for (LaiXe laiXe : danhSachLaiXe) {
            if (laiXe.getMaLX() == maLX) {
                return laiXe;
            }
        }
        return null;
    }

    private static Tuyen timTuyenTheoMa(int maTuyen) {
        for (Tuyen tuyen : danhSachTuyen) {
            if (tuyen.getMaTuyen() == maTuyen) {
                return tuyen;
            }
        }
        return null;
    }

    private static void sapXepDanhSachPhanCong() {
        if (bangPhanCong.isEmpty()) {
            System.out.println("Danh sách phân công hiện đang trống.");
            return;
        }

        System.out.println("\n===== MENU SAP XEP =====");
        System.out.println("1. Theo Họ tên lái xe");
        System.out.println("2. Theo Số lượng tuyến đảm nhận trong ngày (giảm dần)");

        System.out.print("Nhập lựa chọn của bạn: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                Collections.sort(bangPhanCong, Comparator.comparing(o -> o.getLaiXe().getHoTen()));
                break;
            case 2:
                Collections.sort(bangPhanCong, Comparator.comparingInt(BangPhanCong::getSoLuot).reversed());
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
                break;
        }

        System.out.println("Danh sách phân công sau khi sắp xếp:");
        for (BangPhanCong phanCong : bangPhanCong) {
            System.out.println(phanCong);
        }
    }

    private static void bangKeKhoangCach() {
        if (bangPhanCong.isEmpty()) {
            System.out.println("Danh sách phân công hiện đang trống.");
            return;
        }

        Map<LaiXe, Double> bangKe = new HashMap<>();
        for (BangPhanCong phanCong : bangPhanCong) {
            double khoangCach = phanCong.getSoLuot() * phanCong.getTuyen().getKhoangCach();
            bangKe.put(phanCong.getLaiXe(), bangKe.getOrDefault(phanCong.getLaiXe(), 0.0) + khoangCach);
        }

        System.out.println("Bảng kê tổng khoảng cách chạy xe trong ngày của mỗi lái xe:");
        for (LaiXe laiXe : bangKe.keySet()) {
            System.out.println("Lái xe: " + laiXe.getHoTen() + ", Tổng khoảng cách: " + bangKe.get(laiXe) + " km");
        }
    }
}