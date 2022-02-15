data class Subject(
    val title: String,
    val grade: Int
)

data class Student(
    val name: String,
    val dateBirth: Int,
    val subject: List<Subject>
    ){
    val age=2022-dateBirth
    val averageGrade: Float
    get()=subject.average{it.grade.toFloat()}
}

fun <T> Iterable<T>.average(block: (T) -> Float): Float {
    var sum: Double = 0.0
    var count: Int = 0
    for (element in this){
        sum += block(element)
        ++count
    }
    return (sum/count).toFloat()
}

class StudentTooYoungException(message: String): Exception(message)

data class University(
    val title: String,
    val students: MutableList<Student>
) {
    val averageGrade:Float
    get()=students.average{it.averageGrade}

    val courses: Map<Int, List<Student>>
    get()=students
        .groupBy{it.age}
        .mapKeys{
        when (it.key) {
            18 -> 1
            19 -> 2
            20 -> 3
            21 -> 4
            else -> throw StudentTooYoungException("Age discrepancy")
        }
        }
}

enum class StudyProgram(val title: String){
    FIM("finite element method"),
    OS("operating systems"),
    PP("parallel programming");

    infix fun withGrade(grade:Int) = Subject(title, grade)
}

typealias StudentsListener = (Student) -> Unit

val students = mutableListOf(
    Student("Vasya",2001,listOf(StudyProgram.FIM withGrade 5, StudyProgram.OS withGrade 4, StudyProgram.PP withGrade 3)),
    Student("Habib",2002,listOf(StudyProgram.FIM withGrade 4, StudyProgram.OS withGrade 4, StudyProgram.PP withGrade 5)),
    Student("Hasbik",2001,listOf(StudyProgram.FIM withGrade 5, StudyProgram.OS withGrade 5, StudyProgram.PP withGrade 5)),
    //Student("Abdurozik",2000,listOf(StudyProgram.FIM withGrade 5, StudyProgram.OS withGrade 5, StudyProgram.PP withGrade 5)),
    Student("Ramzan",2002,listOf(StudyProgram.FIM withGrade 3, StudyProgram.OS withGrade 4, StudyProgram.PP withGrade 3)),
    Student("Erema",2003,listOf(StudyProgram.FIM withGrade 5, StudyProgram.OS withGrade 4, StudyProgram.PP withGrade 5)),
    Student("Ilya",2004,listOf(StudyProgram.FIM withGrade 4, StudyProgram.OS withGrade 4, StudyProgram.PP withGrade 3)),
    Student("Daniel",2003,listOf(StudyProgram.FIM withGrade 4, StudyProgram.OS withGrade 4, StudyProgram.PP withGrade 4))
)


object DataSource {
    val university: University by lazy {
        University("mgy", students)
    }

    var onNewStudentListener: StudentsListener? = null

    fun addStudent(name: String, birthYear: Int, students: MutableList<Student>) {
        students.add(Student(name, birthYear, listOf(StudyProgram.FIM withGrade 3, StudyProgram.OS withGrade 3)))
        val addedStudent = students.last()
        onNewStudentListener?.invoke(addedStudent)
    }
}
fun main() {
    DataSource.onNewStudentListener = {
        println("Новый студент: $it " + "Средняя оценка по университету ${DataSource.university.averageGrade}")
    }
    println("university average:")
    println(DataSource.university.averageGrade)
    println(DataSource.university.courses)
    DataSource.addStudent("Eger", 2002, students)
}
