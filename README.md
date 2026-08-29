# VortexVM Android

**VortexVM** is a high-performance virtual machine manager and control dashboard engineered for running Windows 11, Windows 10, Linux, and custom guest operating systems directly on Android devices with optimized resource allocation and hardware acceleration.

Created by **FourgeAI LABS** ([https://github.com/fourgeailabs](https://github.com/fourgeailabs)).

---

## 🚀 Key Features

- **Windows 11 Optimization**:
  - Pre-tuned profile wizard supporting Windows 11 ARM64 and x86_64.
  - Built-in TPM 2.0 bypass and SecureBoot simulation.
  - VirtIO high-performance disk and network driver support.
  - VirGL 3D GPU acceleration & Turnip Vulkan driver translation layers for smooth gaming and productivity.

- **Guest OS Power Dashboard**:
  - Live power state management: **Power On**, **Graceful Shutdown**, **Hard Reset**, **Pause**, **Suspend / Save Snapshot**, and **Power Off**.
  - Real-time telemetry monitoring: CPU utilization graphs, RAM usage gauge, VRAM buffers, Disk I/O, and Virtual Thermal metrics.
  - Interactive Virtual Monitor Display with integrated touch trackpad, gesture controls, and floating gamepad overlay.

- **Hardware Acceleration & Resource Allocation**:
  - Native Linux kernel KVM detection (`/dev/kvm`).
  - Granular CPU thread pinning and governor tuning (Performance, Schedutil).
  - Dynamic memory allocation (up to 12GB RAM) with dynamic ZRAM swap configuration.
  - Audio backend selection (AAudio, OpenSL ES, PulseAudio).

- **Integrated File System Management**:
  - Virtual Disk Manager: Create, resize, compress `.qcow2` and `.img` drive images.
  - Host-to-Guest Shared Storage Mapping: Pass Android storage folders (`/sdcard/VortexShared`) into guest OS (`Z:\Shared`).
  - Virtual Transfer Bridge for seamless file imports and exports.

---

## 📱 App Version

- **Current Version**: `1.02.00`
- **Application ID**: `com.fourgeailabs.vortexvm`

---

## 📋 What's New in Version 1.02.00

### Release Notes (v1.02.00)
- **Engine & Performance Updates**:
  - Full support for 12GB RAM allotment across virtual machine profiles and guest engine slider settings.
  - Implemented display cutout hole punch edge-to-edge layout support.
  - Refreshed Vortex Cyber-Neon UI theme with glassmorphic cards and dynamic telemetry.
  - Integrated KVM Hardware Acceleration & VirGL 3D graphics pipeline with Mesa Turnip Vulkan 1.3 drivers.
  - Integrated File System Manager for QCOW2 image manipulation and shared folder mapping.
  - Added "What's New" expandable accordions with historical update logging.
  - Added About page referencing **FourgeAI LABS** ([https://github.com/fourgeailabs](https://github.com/fourgeailabs)).

---

## 🛠️ Build & Installation

To build the project locally using Gradle:

```bash
./gradlew assembleDebug
```

The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`. Automated builds are powered by `.github/workflows/build.yml`.
