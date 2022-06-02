import com.jlpay.plugin.HenCoderExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * 自定义Task任务，可参考 {@link com.tencent.tinker.build.gradle.task.TinkerPatchSchemaTask}
 */
public class HenCoderTask extends DefaultTask {

    HenCoderExtension henCoderExtension

    //def 变量的定义
    def android

    HenCoderTask() {
        description = "my custom task"
        android = project.extensions.android
    }

    //运行任务就会运行这个方法(方法名无所谓，主要是这个注解标注就行)
    @TaskAction
    def myCustomerTask() {
        //gradle中调用命令行的方式，未完成
//        project.exec {
//
//        }
    }
}