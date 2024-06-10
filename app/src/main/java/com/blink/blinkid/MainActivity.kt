package com.blink.blinkid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.blink.blinkid.ui.teacher.AddExamScreen
import com.blink.blinkid.ui.AddStudentScreen
import com.blink.blinkid.ui.teacher.ExamDetailsScreen
import com.blink.blinkid.ui.theme.BlinkIdTheme
import com.blink.blinkid.ui.teacher.ExamListScreen
import com.blink.blinkid.ui.teacher.HomeScreen
import com.blink.blinkid.ui.LoginScreen
import com.blink.blinkid.ui.teacher.StudentExamVerificationScreen
import com.blink.blinkid.ui.StudentListScreen
import com.blink.blinkid.ui.staff.AddGroupScreen
import com.blink.blinkid.ui.staff.GroupDetailsScreen
import com.blink.blinkid.ui.staff.GroupListScreen
import com.blink.blinkid.ui.staff.StaffHomeScreen
import com.blink.blinkid.ui.staff.StudentGroupVerificationScreen
import com.blink.blinkid.ui.student.StudentDashBoard
import com.blink.blinkid.viewmodel.ExamViewModel
import com.blink.blinkid.viewmodel.GroupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlinkIdTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val examViewModel: ExamViewModel = hiltViewModel()
    val groupViewModel: GroupViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Navigation.Routes.LOGIN
    ) {
        composable(Navigation.Routes.LOGIN) {
            LoginScreen(navController, loginViewModel = hiltViewModel())
        }


        // Student HOme
        composable(Navigation.Routes.STUDENT_DASHBOARD) {
            StudentDashBoard(navController, loginViewModel = hiltViewModel())
        }

        composable(Navigation.Routes.STUDENT_LIST) {
            StudentListScreen(navController, examViewModel)
        }
        composable(Navigation.Routes.ADD_STUDENT) {
            AddStudentScreen(navController)
        }

        // Teacher home
        composable(Navigation.Routes.HOME) {
            HomeScreen(navController, loginViewModel = hiltViewModel())
        }

        composable(Navigation.Routes.EXAMS) {
            ExamListScreen(navController, examViewModel)
        }
        composable(Navigation.Routes.ADD_EXAM) {
            AddExamScreen(navController)
        }
        composable(Navigation.Routes.EXAM_DETAIL + "/{examId}") {
            ExamDetailsScreen(navController, examViewModel)
        }
        composable(Navigation.Routes.STUDENT_EXAM_VERIFICATION) {
            StudentExamVerificationScreen(navController, examViewModel)
        }


        // Staff home
        composable(Navigation.Routes.STAFF_HOME) {
            StaffHomeScreen(navController, loginViewModel = hiltViewModel())
        }

        composable(Navigation.Routes.GROUPS) {
            GroupListScreen(navController, groupViewModel)
        }
        composable(Navigation.Routes.ADD_GROUP) {
            AddGroupScreen(navController)
        }
        composable(Navigation.Routes.GROUP_DETAIL + "/{examId}") {
            GroupDetailsScreen(navController, groupViewModel)
        }
        composable(Navigation.Routes.STUDENT_GROUP_VERIFICATION) {
            StudentGroupVerificationScreen(navController, groupViewModel)
        }
    }
}


object Navigation {

    object Args {
        const val USER_ID = "user_id"
    }

    object Routes {
        const val LOGIN = "login"


        const val STUDENT_LIST = "student_list"
        const val ADD_STUDENT = "add_student"
        const val STUDENT_DASHBOARD = "student_dashboard"


        const val HOME = "home"
        const val EXAMS = "exams"
        const val EXAM_DETAIL = "examDetail"
        const val ADD_EXAM = "add_exam"
        const val STUDENT_EXAM_VERIFICATION = "student_exam_verification"

        const val STAFF_HOME = "staff_home"
        const val GROUPS = "GROUPs"
        const val GROUP_DETAIL = "GROUPDetail"
        const val ADD_GROUP = "add_GROUP"
        const val STUDENT_GROUP_VERIFICATION = "student_group_verification"

    }

}


