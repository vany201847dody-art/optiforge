package com.vizor.optiforge;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class HardwareDetector {

    public static boolean detected = false;
    public static int cpuCores;
    public static double cpuFreqMhz;
    public static long totalRamMb;
    public static boolean isIntegratedGpu;
    public static String gpuName = "unknown";
    public static int tier = 2; // 0=поток,1=слабо,2=средне,3=хорошо,4=ультра

    public static void detect() {
        try {
            Runtime rt = Runtime.getRuntime();
            long maxBytes = rt.maxMemory();
            totalRamMb = Math.max(1024, maxBytes / (1024L * 1024L));

            cpuCores = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
            if (cpuCores < 1) cpuCores = 2;

            cpuFreqMhz = readCpuFrequency();

            isIntegratedGpu = isIntegratedGpu();

            computeTier();
            detected = true;
        } catch (Throwable e) {
            cpuCores = 2;
            cpuFreqMhz = 2000;
            totalRamMb = 4000;
            isIntegratedGpu = true;
            tier = 2;
            detected = true;
        }
    }

    private static double readCpuFrequency() {
        try {
            // Попытка прочитать частоту из /proc/cpuinfo (среднее по "cpu MHz")
            java.nio.file.Path p = java.nio.file.Paths.get("/proc/cpuinfo");
            if (java.nio.file.Files.exists(p)) {
                java.util.List<String> lines = java.nio.file.Files.readAllLines(p);
                double sum = 0;
                int count = 0;
                for (String line : lines) {
                    int idx = line.indexOf("cpu MHz");
                    if (idx >= 0) {
                        int colon = line.indexOf(':', idx);
                        if (colon >= 0) {
                            try {
                                sum += Double.parseDouble(line.substring(colon + 1).trim());
                                count++;
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }
                if (count > 0) return sum / count;
            }
        } catch (Throwable ignored) {
        }
        // Осень: оцениваем по количеству ядер как приближение
        return cpuCores <= 2 ? 2000 : cpuCores <= 4 ? 2500 : 3000;
    }

    private static boolean isIntegratedGpu() {
        String lower = gpuName == null ? "" : gpuName.toLowerCase();
        return lower.contains("intel") || lower.contains("hd graphics")
                || lower.contains("radeon") || lower.contains("vega") || lower.contains("iris")
                || lower.contains("integrated") || lower.contains("amdz") || lower.contains("amdz");
    }

    public static void setGpuName(String name) {
        if (name != null && !name.isEmpty()) {
            gpuName = name;
            isIntegratedGpu = isIntegratedGpu();
            computeTier();
        }
    }

    private static void computeTier() {
        int score = 0;
        if (cpuCores >= 8) score += 3; else if (cpuCores >= 6) score += 2; else if (cpuCores >= 4) score += 1;
        if (cpuFreqMhz >= 3500) score += 3; else if (cpuFreqMhz >= 2500) score += 2; else if (cpuFreqMhz >= 1500) score += 1;
        if (totalRamMb >= 16000) score += 3; else if (totalRamMb >= 8000) score += 2; else if (totalRamMb >= 4000) score += 1;
        if (isIntegratedGpu) score -= 2; else score += 2;

        if (score <= 2) tier = 0;
        else if (score <= 4) tier = 1;
        else if (score <= 6) tier = 2;
        else if (score <= 8) tier = 3;
        else tier = 4;
    }

    public static boolean isDetected() {
        return detected;
    }

    public static String getTierName() {
        switch (tier) {
            case 0: return "Поток (очень слабо)";
            case 1: return "Слабое";
            case 2: return "Среднее";
            case 3: return "Хорошее";
            default: return "Ультра";
        }
    }

    public static String getSummary() {
        return "CPU: " + cpuCores + " яд., " + (int) cpuFreqMhz + " МГц | RAM: " + (int) totalRamMb + " МБ | GPU: "
                + (isIntegratedGpu ? "встроенная" : "дискретная");
    }
}
