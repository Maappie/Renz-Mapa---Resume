/**
 * carousel.js — Lightweight vanilla carousel for the Projects section.
 * Handles arrow navigation, dot indicators, and keyboard support.
 */
(function () {
    const track      = document.getElementById('carousel-track');
    const prevBtn    = document.getElementById('carousel-prev');
    const nextBtn    = document.getElementById('carousel-next');
    const dotsWrap   = document.getElementById('carousel-dots');

    if (!track || !prevBtn || !nextBtn) return;

    const cards = Array.from(track.querySelectorAll('.project-card'));
    const dots  = dotsWrap ? Array.from(dotsWrap.querySelectorAll('.carousel-dot')) : [];
    let current = 0;

    function goTo(index) {
        // Clamp
        index = Math.max(0, Math.min(index, cards.length - 1));
        current = index;

        // Slide track
        const cardWidth = cards[0].offsetWidth + 24; // 24 = gap
        track.style.transform = `translateX(-${current * cardWidth}px)`;

        // Update dots
        dots.forEach((d, i) => d.classList.toggle('active', i === current));

        // Disable / enable arrows
        prevBtn.disabled = current === 0;
        nextBtn.disabled = current === cards.length - 1;
    }

    prevBtn.addEventListener('click', () => goTo(current - 1));
    nextBtn.addEventListener('click', () => goTo(current + 1));

    // Dot clicks
    dots.forEach((dot, i) => dot.addEventListener('click', () => goTo(i)));

    // Keyboard navigation
    document.addEventListener('keydown', (e) => {
        if (e.key === 'ArrowLeft')  goTo(current - 1);
        if (e.key === 'ArrowRight') goTo(current + 1);
    });

    // Recalculate on resize
    window.addEventListener('resize', () => goTo(current));

    // Init
    goTo(0);
})();
