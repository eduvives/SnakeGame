# Snake Game &nbsp;&nbsp;<img src="https://github.com/user-attachments/assets/87a18f14-fadc-4a96-8e34-f0bbf918c40c" width="50">

Recreación de la versión de Google del tradicional juego Snake, desarrollada en lenguaje java con una interfaz gráfica atractiva y siguiendo el patrón de diseño MVC (Modelo-Vista-Controlador), buscando proporcionar diversión y comodidad.

<p align="center">
  <img src="https://github.com/user-attachments/assets/2eb4e75b-7a25-49e1-b6fd-3af0a3c74908" width="525"/>
</p>

## Cómo Empezar a Jugar

En la sección de lanzamientos (**_releases_**) encontrarás el **archivo ejecutable del juego** (_.exe_) perteneciente a la versión correspondiente.

Se recomienda descargar la última versión (v4) para disfrutar de las nuevas funcionalidades en su totalidad.

## Modos de Juego

Incluye los modos de juego:

- **Classic**: La **versión tradicional** del juego. La serpiente crece al comer y el objetivo es obtener la mayor puntuación sin chocar contra las paredes o la propia serpiente.

- **Wall**: Aparecen **muros fijos** en el tablero que crean **obstáculos adicionales**. Evita chocar contra ellos mientras intentas conseguir la máxima puntuación.

- **Cheese**: La serpiente está formada por **secciones abiertas y cerradas**, lo que permite que la serpiente pase **a través de sí misma**. Cada vez que come, crece el doble que en el modo clásico, obteniendo tanto una sección abierta como una cerrada.

- **Boundless**: **No hay bordes**. Si la serpiente sale por un lado del tablero, reaparece en el lado opuesto, creando un efecto de **tablero infinito**.

- **Shrink**: Las **colisiones no terminan el juego** de inmediato. Cada vez que la serpiente choca con una pared o su propio cuerpo, su **longitud disminuye**. La partida termina cuando la serpiente se reduce a un tamaño inferior al inicial.

- **Twin**: Después de comer una fruta, la **cabeza de la serpiente se convierte en su cola**, y viceversa. Esto provoca un cambio de dirección y requiere adaptarse rápidamente a la nueva posición. Hay una pequeña pausa para ajustar el movimiento después de cada cambio.

- **Statue**: Cada vez que la serpiente come, deja una **estatua de su propio cuerpo** en el lugar donde estaba al comer. Estas estatuas actúan como nuevos obstáculos que debes evitar. Las estatuas **se rompen** a medida que se comen frutas.

- **Dimension**: Existen **dos dimensiones**, una visible y otra oculta. La comida y la serpiente **cambian entre dimensiones al comer**. Mientras una parte del cuerpo está en una dimensión, **no puede interactuar** con los elementos de la otra.

  _Dependiendo de los modos de juego que tengas activos a través del Modo Blender, otros elementos como muros y estatuas también aparecerán en la otra dimensión._

- **Peaceful**: Modo **sin colisiones**, en el cual los bordes se comportan como en el modo **Boundless**. Puedes relajarte y jugar sin preocuparte por chocar con las paredes o la serpiente misma.

  _El juego solo finaliza por "festín" o presionando la tecla "ESCAPE"._

- **Blender**: Te permite **combinar diferentes modos de juego**. Puedes personalizar la experiencia seleccionando los modos que deseas activar al mismo tiempo para crear tu propia versión del juego.

_El juego termina automáticamente si la serpiente choca o si se alcanza la condición de "**festín**", que ocurre cuando ya no hay más espacios disponibles para colocar comida en el tablero._

_Puedes decidir terminar la partida en cualquier momento, sin esperar a ganar o perder, simplemente presionando la tecla "**ESCAPE**". Al hacerlo, el juego finalizará y se mostrará el menú principal._

<p align="center">
  <img src="https://github.com/user-attachments/assets/d5532f58-2d3d-4e3d-9b1e-c358ee77b77b" width="800"/>
</p>

## Interfaz Gráfica

### Paneles de Navegación

- **Tablero de juego**: Aquí es donde se muestran la serpiente, la comida y los elementos específicos de ciertos modos de juego, como muros (Wall) y estatuas (Statue).

- **Menú superior**: A la izquierda se indica la puntuación actual y la más alta (high score). Desde el lado derecho puedes cambiar la paleta de colores del juego, ajustando su apariencia a tu gusto.

- **Menú principal**: Muestra la puntuación final y la más alta de todas las partidas. Además, desde aquí puedes iniciar una nueva partida, cambiar la configuración de partida o salir del juego.

- **Configuración de partida**: Permite personalizar varios aspectos del juego, como el **tamaño del tablero** (con opciones de fácil, mediano y difícil), la **velocidad de la serpiente** (lenta, normal o rápida), la **cantidad de frutas** disponibles (1, 3, 5 o aleatoria) y el **modo de juego**. También puedes aleatorizar las configuraciones, restablecer los valores por defecto e iniciar una nueva partida fácilmente.

- **Configuración del modo Blender**: Permite seleccionar los diferentes modos de juego que se desean combinar en la siguiente partida. Incluye opciones para aleatorizar estas combinaciones (con diferentes probabilidades de seleccionar entre 2 y 4 modos), restablecer la selección por defecto (siendo esta la selección aleatoria) e iniciar una nueva partida con las selecciones realizadas.

<p align="center">
  <img src="https://github.com/user-attachments/assets/532fd828-3b2c-45f5-a04b-fefbbf98dbfa" width="830"/>
</p>

### Personalización de Colores

En el menu superior de la ventana principal, hay 3 botones para seleccionar una **paleta de colores** distinta para los siguientes elementos:

- **Tablero** (color de los muros - Wall)
- **Comida**
- **Serpiente** (color de la estatua - Statue)

<p align="center">
  <img src="https://github.com/user-attachments/assets/f1e6889c-26a4-4c11-8a5b-989e3289a0c0" width="800"/>
</p>

_Solo es posible realizar cambios de color cuando la partida no haya empezado, es decir, mientras el usuario no haya presionado una tecla iniciando así el movimiento de la serpiente. En cuanto comienza una nueva partida esta funcionalidad se desactiva hasta que haya finalizado._

### Ajuste del Tamaño de la Ventana

Para una visibilidad óptima, puedes **ampliar o reducir el tamaño de la ventana** en cualquier nivel de dificultad simplemente **haciendo doble clic sobre ella**. Esto permite adaptar el juego a la pantalla según prefieras.

Hay dos opciones de tamaño:

- **Tamaño inicial**: La ventana mide 650 x 650 píxeles.
- **Tamaño ampliado**: La altura de la ventana ocupa un 92% de la pantalla disponible.

## Movimiento de la Serpiente

Los movimientos de la serpiente se controlan usando las **flechas del teclado** o las teclas _**W, A, S y D**_.

Cada vez que se pulsa una de estas teclas, se genera una acción de cambio de dirección. Estas acciones se almacenan y se gestionan en una lista que puede contener **hasta dos movimientos pendientes**.

Si se realiza una nueva acción cuando la lista ya está llena, esta **reemplazará al último movimiento** almacenado.

Si se intenta mover la serpiente en la **misma dirección o en la opuesta** a la actual, la acción es **ignorada**. Esto nos asegura que solo se guardan movimientos que realmente cambian la trayectoria de la serpiente.

Este sistema garantiza un **control rápido, fluido y preciso**, mejorando la experiencia de juego.

### Movimientos de Reserva para el Modo Shrink

Cuando el modo **Shrink** está activado, el juego guarda los movimientos no válidos que el jugador intenta realizar. Esto sirve para que, en caso de que la serpiente choque, no pueda moverse en **direcciones incoherentes** (opuesta a su cuerpo...).

Si ocurre una colisión, todos los movimientos que el jugador haya hecho hasta ese momento se **revisan**, y solo se utiliza el **último movimiento que sea válido** después del choque. Esto permite que el jugador corrija la dirección de la serpiente más rápidamente.

## Registro de Puntuaciones más Altas

Se guarda la **puntuación más alta** obtenida en **cada partida jugada**.

Las puntuaciones se organizan según la **configurado de la partida** (Board, Speed, Food y Mode). En el modo **Blender**, también se toman en cuenta los **modos elejidos**.

Si superas tu mejor puntuación en una configuración específica, esta nueva puntuación **reemplazará a la anterior**. Así, se mantendrá un registro de la mejor puntuación para cada combinación de ajustes que hayas utilizado.

_Estas puntuaciones se almacenan en un fichero de texto llamado "snake_highscores", el cual es creado automáticamente en el directorio donde se ejecute el programa. Cada puntuación tiene un identificador que permite saber a qué configuración del juego corresponde._

_El panel de puntuación más altas es visible en todos los modos de juego excepto en el modo Clásico. Esto permite a los jugadores ver su mejor puntuación en todo momento en los demás modos. En el modo Clásico el panel estará oculto si no hay una puntuación anterior registrada, es decir, si la mejor puntuación es cero al comienzo de la partida. Si el panel no se muestra significa que la puntuación actual representa también la puntuación más alta._
