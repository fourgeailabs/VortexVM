package com.example

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.CreateVmScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FileSystemScreen
import com.example.ui.screens.HardwareAccelScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.VirtualDisplayScreen
import com.example.ui.screens.WhatsNewScreen
import com.example.ui.theme.DarkVoidBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.viewmodel.VmViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge drawing
        enableEdgeToEdge()
        
        // Allow drawing behind display cutout / camera notch hole punch
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        setContent {
            MyApplicationTheme(darkTheme = true, dynamicColor = false) {
                VortexVmApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VortexVmApp() {
    val navController = rememberNavController()
    val viewModel: VmViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "dashboard"

    val showBottomBar = currentRoute in listOf("dashboard", "hardware_accel", "file_system", "settings")

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF030712),
                        Color(0xFF0B132B),
                        Color(0xFF030712)
                    )
                )
            ),
        containerColor = Color.Transparent,
        topBar = {
            if (showBottomBar) {
                TopAppBar(
                    title = {
                        Text(
                            text = when (currentRoute) {
                                "dashboard" -> "Vortex VM Hyper-Drive"
                                "hardware_accel" -> "Vulkan 1.3 & 12GB RAM Engine"
                                "file_system" -> "QCOW2 Disks & Shared Storage"
                                "settings" -> "System Settings"
                                else -> "Vortex VM"
                            },
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF030712).copy(alpha = 0.85f),
                        titleContentColor = Color.White
                    ),
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color(0xFF030712).copy(alpha = 0.95f),
                    contentColor = Color.White,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    NavigationBarItem(
                        selected = currentRoute == "dashboard",
                        onClick = {
                            navController.navigate("dashboard") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.DesktopWindows, contentDescription = "Dashboard") },
                        label = { Text("VMs") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonCyan,
                            selectedTextColor = NeonCyan,
                            indicatorColor = Color(0xFF0284C7).copy(alpha = 0.3f),
                            unselectedIconColor = Color(0xFF9CA3AF),
                            unselectedTextColor = Color(0xFF9CA3AF)
                        ),
                        modifier = Modifier.testTag("nav_dashboard")
                    )

                    NavigationBarItem(
                        selected = currentRoute == "hardware_accel",
                        onClick = {
                            navController.navigate("hardware_accel") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Speed, contentDescription = "Vulkan Accel") },
                        label = { Text("Vulkan") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonCyan,
                            selectedTextColor = NeonCyan,
                            indicatorColor = Color(0xFF0284C7).copy(alpha = 0.3f),
                            unselectedIconColor = Color(0xFF9CA3AF),
                            unselectedTextColor = Color(0xFF9CA3AF)
                        ),
                        modifier = Modifier.testTag("nav_hardware_accel")
                    )

                    NavigationBarItem(
                        selected = currentRoute == "file_system",
                        onClick = {
                            navController.navigate("file_system") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Folder, contentDescription = "File System") },
                        label = { Text("Disks") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonCyan,
                            selectedTextColor = NeonCyan,
                            indicatorColor = Color(0xFF0284C7).copy(alpha = 0.3f),
                            unselectedIconColor = Color(0xFF9CA3AF),
                            unselectedTextColor = Color(0xFF9CA3AF)
                        ),
                        modifier = Modifier.testTag("nav_file_system")
                    )

                    NavigationBarItem(
                        selected = currentRoute == "settings",
                        onClick = {
                            navController.navigate("settings") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonCyan,
                            selectedTextColor = NeonCyan,
                            indicatorColor = Color(0xFF0284C7).copy(alpha = 0.3f),
                            unselectedIconColor = Color(0xFF9CA3AF),
                            unselectedTextColor = Color(0xFF9CA3AF)
                        ),
                        modifier = Modifier.testTag("nav_settings")
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") {
                DashboardScreen(
                    viewModel = viewModel,
                    onCreateVmClick = { navController.navigate("create_vm") },
                    onOpenDisplayClick = { vmId -> navController.navigate("virtual_display/$vmId") },
                    onHardwareAccelClick = { navController.navigate("hardware_accel") },
                    onFileSystemClick = { navController.navigate("file_system") },
                    onOpenVmDetailClick = { vmId -> navController.navigate("virtual_display/$vmId") }
                )
            }

            composable("create_vm") {
                CreateVmScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = "virtual_display/{vmId}",
                arguments = listOf(navArgument("vmId") { type = NavType.LongType })
            ) { backStackEntry ->
                val vmId = backStackEntry.arguments?.getLong("vmId") ?: 1L
                VirtualDisplayScreen(
                    vmId = vmId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("hardware_accel") {
                HardwareAccelScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("file_system") {
                FileSystemScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("settings") {
                SettingsScreen(
                    viewModel = viewModel,
                    onNavigateToWhatsNew = { navController.navigate("whats_new") },
                    onNavigateToAbout = { navController.navigate("about") },
                    onNavigateToHardwareAccel = { navController.navigate("hardware_accel") },
                    onNavigateToFileSystem = { navController.navigate("file_system") }
                )
            }

            composable("whats_new") {
                WhatsNewScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("about") {
                AboutScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
