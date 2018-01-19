import java.io.IOException;
import java.nio.file.*;

/**
 * 检测文件变化，采用WatchService方式
 */
public class Test {
    public static void main(String[] a) {

        final Path path = Paths.get("f:\\test\\");

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            //给path路径加上文件观察服务
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);
            while (true) {
                final WatchKey key = watchService.take();

                for (WatchEvent<?> watchEvent : key.pollEvents()) {

                    final WatchEvent.Kind<?> kind = watchEvent.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }
                    //创建事件
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        System.out.println("[新建]");
                    }
                    //修改事件
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        System.out.println("修改]");
                    }
                    //删除事件
                    if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        System.out.println("[删除]");
                    }
                    // get the filename for the event
                    final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
                    final Path filename = watchEventPath.context();
                    // print it out
                    System.out.println(kind + " -> " + filename);

                }
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }

        } catch (IOException | InterruptedException ex) {
            System.err.println(ex);
        }

    }
}
