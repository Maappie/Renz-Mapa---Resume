/**
 * contactForm.js — UI Component
 *
 * Responsibility: validate the contact form and hand the message off to the
 * visitor's email client.
 *
 * There is no message-storing endpoint in the API, so the form composes a
 * mailto: link rather than pretending to send anything. The note under the
 * button says exactly that, so nobody is misled about what "Send" does.
 */

const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

/**
 * Writes a status line under the submit button.
 *
 * @param {HTMLElement|null} note - the note element
 * @param {string} message - text to show
 * @param {'idle'|'error'|'success'} [state='idle'] - styling state
 */
function setNote(note, message, state = 'idle') {
    if (!note) return;
    note.textContent = message;
    note.classList.toggle('is-error', state === 'error');
    note.classList.toggle('is-success', state === 'success');
}

/**
 * Wires the contact form's submit handler.
 *
 * @param {HTMLFormElement} form - the form element
 * @param {string|null} recipient - address to send to, or null if unknown
 */
export function initContactForm(form, recipient) {
    if (!form) return;

    const note = form.querySelector('#contact-note');

    form.addEventListener('submit', event => {
        event.preventDefault();

        const name = form.querySelector('#contact-name').value.trim();
        const from = form.querySelector('#contact-from').value.trim();
        const message = form.querySelector('#contact-message').value.trim();

        if (!recipient) {
            setNote(note, 'Email address unavailable right now — please use the quick links instead.', 'error');
            return;
        }

        if (!name || !from || !message) {
            setNote(note, 'Please fill in your name, email and message.', 'error');
            return;
        }

        if (!EMAIL_PATTERN.test(from)) {
            setNote(note, 'That email address doesn\'t look right.', 'error');
            return;
        }

        const subject = `Portfolio enquiry from ${name}`;
        const body = `${message}\n\n—\n${name}\n${from}`;
        const mailto = `mailto:${recipient}`
            + `?subject=${encodeURIComponent(subject)}`
            + `&body=${encodeURIComponent(body)}`;

        window.location.href = mailto;
        setNote(note, 'Opening your email app with the message ready to send…', 'success');
    });
}
