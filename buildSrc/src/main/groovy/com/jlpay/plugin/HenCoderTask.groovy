import com.jlpay.plugin.HenCoderExtension
import org.gradle.api.DefaultTask

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

    def myCustomerTask() {

    }
}