// ─────────────────────────────────────────────
//  inputSanitizer.js  –  Global input filters
// ─────────────────────────────────────────────

/**
 * Username: solo lettere, cifre e underscore.
 * Niente trattini → niente possibilità di formare "--".
 */
function sanitizeUsername(value) {
    if (typeof value !== 'string') return '';
    return value
        .replace(/[^a-zA-Z0-9_]/g, '')   // whitelist stretta, NO trattino
        .slice(0, 32)
        .trim();
}

/**
 * Email: whitelist RFC-safe.
 */
function sanitizeEmail(value) {
    if (typeof value !== 'string') return '';
    return value
        .replace(/[^a-zA-Z0-9._%+\-@]/g, '')
        .slice(0, 254)
        .trim();
}

/**
 * Password: non tocca i simboli legittimi (servono per la robustezza),
 * ma blocca null bytes, controllo ASCII e tag HTML/script.
 * La vera protezione è bcrypt + prepared statements lato server.
 */
function sanitizePassword(value) {
    if (typeof value !== 'string') return '';
    return value
        .replace(/[\x00-\x1F\x7F]/g, '')   // control chars + null byte
        .replace(/</g, '')                  // blocca injection HTML/script
        .replace(/>/g, '')
        .slice(0, 128);
}

/**
 * Testo generico (es. nomi, descrizioni): lettere, cifre, spazi,
 * punteggiatura comune. Niente apici, backtick, punto e virgola o backslash.
 */
function sanitizeText(value) {
    if (typeof value !== 'string') return '';
    return value
        .replace(/[^a-zA-Z0-9\s.,!?@()\-]/g, '')  // whitelist esplicita
        .replace(/\s{2,}/g, ' ')                   // spazi multipli → uno
        .slice(0, 255)
        .trim();
}

/**
 * Helper: sanitizza un intero oggetto form in una sola chiamata.
 *
 * @param {Object} fields   - { fieldName: 'tipo' }  tipo ∈ email|username|password|text
 * @param {Object} formData - { fieldName: rawValue }
 * @returns {Object} oggetto con valori sanitizzati
 *
 * @example
 *   const clean = sanitizeForm(
 *     { email: 'email', username: 'username', password: 'password' },
 *     { email: rawEmail, username: rawUser, password: rawPass }
 *   );
 */
function sanitizeForm(fields, formData) {
    const sanitizers = {
        email:    sanitizeEmail,
        username: sanitizeUsername,
        password: sanitizePassword,
        text:     sanitizeText,
    };

    const result = {};
    for (const [field, type] of Object.entries(fields)) {
        const fn = sanitizers[type] || sanitizeText;
        result[field] = fn(formData[field] ?? '');
    }
    return result;
}