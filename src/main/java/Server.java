import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    // Non-Blocking вариант был выбран так-как до получения искомого значения работа программы может продолжаться
    public static void main(String[] args) throws IOException {

        final ServerSocketChannel serverChannel = ServerSocketChannel.open();

        serverChannel.bind(new InetSocketAddress("localhost", 56000));

        while (true) {

            try (SocketChannel socketChannel = serverChannel.accept()) {

                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);

                    if (bytesCount == -1) break;

                    final String inputString = new String(inputBuffer.array(), 0,
                            bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    System.out.println("Исходный текст: " + inputString);

                    socketChannel.write(ByteBuffer.wrap((inputString.replaceAll(" +", " "))
                            .getBytes(StandardCharsets.UTF_8)));
                }

            }

        }
    }

}
