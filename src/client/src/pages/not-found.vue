<template>
    <v-container class="d-flex flex-column align-center justify-center text-center h-100">
        <v-row>
            <v-col>
                <h1>404 - Страница не найдена</h1>
                <p>К сожалению, запрашиваемая страница отсутствует</p>
            </v-col>
        </v-row>
        <v-row>
            <v-col>
                <v-btn class="button-primary" to="/main" variant="text">
                    На главную
                </v-btn>
            </v-col>
        </v-row>
        <v-row>
            <canvas id="bouncingCanvas" ></canvas>
        </v-row>
    </v-container>
</template>

<script setup>
onMounted(() => {
    const canvas = document.getElementById('bouncingCanvas');
    const ctx = canvas.getContext('2d');

    canvas.width = window.innerWidth * 0.7;
    canvas.height = window.innerHeight * 0.6;

    const imageUrl = '/img.png';
    const image = new Image();
    let x = 100, y = 100;
    let dx = 5, dy = 5;
    const imgWidth = 100, imgHeight = 100; 
    const borderProximity = 3; 

    image.src = imageUrl;
    image.onload = () => {
        requestAnimationFrame(animate);
    };


    function animate() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        if (x <= borderProximity) {
            drawLine(0, 0, 0, canvas.height, 'red');
        }
        if (x + imgWidth >= canvas.width - borderProximity) {
            drawLine(canvas.width - 1, 0, canvas.width - 1, canvas.height, 'red');
        }
        if (y <= borderProximity) {
            drawLine(0, 0, canvas.width, 0, 'red');
        }
        if (y + imgHeight >= canvas.height - borderProximity) {
            drawLine(0, canvas.height - 1, canvas.width, canvas.height - 1, 'red');
        }

        ctx.drawImage(image, x, y, imgWidth, imgHeight);

        x += dx;
        y += dy;

        if (x <= 0 || x + imgWidth >= canvas.width) {
            dx = -dx;
        }
        if (y <= 0 || y + imgHeight >= canvas.height) {
            dy = -dy;
        }

        requestAnimationFrame(animate);
    }

    function drawLine(x1, y1, x2, y2, color) {
        ctx.beginPath();
        ctx.moveTo(x1, y1);
        ctx.lineTo(x2, y2);
        ctx.strokeStyle = color;
        ctx.lineWidth = 2;
        ctx.stroke();
    }

    window.addEventListener('resize', () => {
        canvas.width = window.innerWidth * 0.8;
        canvas.height = window.innerHeight * 0.8;
    });
});
</script>
