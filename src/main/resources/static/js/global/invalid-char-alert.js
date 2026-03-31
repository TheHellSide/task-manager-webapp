(function () {
    const style = document.createElement('style');
    style.textContent = `
        #invalid-char-overlay {
            display: none;
            position: fixed;
            inset: 0;
            z-index: 9999;
            justify-content: center;
            align-items: center;
            background: rgba(0, 0, 0, 0.35);
            backdrop-filter: blur(3px);
            -webkit-backdrop-filter: blur(3px);
            animation: overlay-in 0.2s ease;
        }

        #invalid-char-overlay.visible {
            display: flex;
        }

        @keyframes overlay-in {
            from { opacity: 0; }
            to   { opacity: 1; }
        }

        #invalid-char-box {
            width: 100%;
            max-width: 380px;
            padding: 2rem;
            border-radius: 1rem;
            box-shadow: 0 0 32px rgba(0,0,0,0.18);
            background-color: var(--bs-body-bg);
            color: var(--bs-body-color);
            position: relative;
            animation: box-in 0.25s cubic-bezier(0.34, 1.56, 0.64, 1);
            text-align: center;
        }

        @keyframes box-in {
            from { transform: scale(0.85); opacity: 0; }
            to   { transform: scale(1);    opacity: 1; }
        }

        #invalid-char-box .alert-icon {
            font-size: 2.4rem;
            margin-bottom: 0.6rem;
            display: block;
            animation: shake 0.4s ease 0.1s both;
        }

        @keyframes shake {
            0%,100% { transform: translateX(0); }
            20%      { transform: translateX(-6px); }
            40%      { transform: translateX(6px); }
            60%      { transform: translateX(-4px); }
            80%      { transform: translateX(4px); }
        }

        #invalid-char-box h5 {
            margin: 0 0 0.4rem;
            font-weight: 600;
            font-size: 1.05rem;
            color: var(--bs-danger, #dc3545);
        }

        #invalid-char-box p {
            font-size: 0.9rem;
            color: var(--bs-secondary-color);
            margin: 0 0 1.3rem;
            line-height: 1.5;
        }

        #invalid-char-box .invalid-chars-list {
            display: inline-flex;
            flex-wrap: wrap;
            gap: 0.35rem;
            justify-content: center;
            margin-bottom: 1.3rem;
        }

        #invalid-char-box .invalid-chars-list span {
            font-family: monospace;
            font-size: 0.85rem;
            background: var(--bs-danger-bg-subtle, #f8d7da);
            color: var(--bs-danger-text-emphasis, #842029);
            border: 1px solid var(--bs-danger-border-subtle, #f1aeb5);
            border-radius: 0.3rem;
            padding: 0.15rem 0.5rem;
        }

        #invalid-char-close {
            width: 100%;
            padding: 0.5rem;
            border: none;
            border-radius: 0.6rem;
            background-color: var(--bs-primary, #0d6efd);
            color: #fff;
            font-size: 0.9rem;
            font-weight: 500;
            cursor: pointer;
            transition: opacity 0.15s ease, transform 0.15s ease;
        }

        #invalid-char-close:hover {
            opacity: 0.88;
            transform: translateY(-1px);
        }

        #invalid-char-close:active {
            transform: translateY(0);
        }
    `;
    document.head.appendChild(style);

    const overlay = document.createElement('div');
    overlay.id = 'invalid-char-overlay';
    overlay.setAttribute('role', 'dialog');
    overlay.setAttribute('aria-modal', 'true');
    overlay.setAttribute('aria-labelledby', 'invalid-char-title');
    overlay.innerHTML = `
        <div id="invalid-char-box">
            <h5 id="invalid-char-title">Caratteri non validi</h5>
            <p id="invalid-char-msg">Il campo contiene caratteri non consentiti.<br>Sono stati rimossi automaticamente.</p>
            <div class="invalid-chars-list" id="invalid-chars-list"></div>
            <button id="invalid-char-close">Capito</button>
        </div>
    `;
    document.body.appendChild(overlay);

    document.getElementById('invalid-char-close').addEventListener('click', hideInvalidCharAlert);
    overlay.addEventListener('click', function (e) {
        if (e.target === overlay) hideInvalidCharAlert();
    });
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') hideInvalidCharAlert();
    });

    function hideInvalidCharAlert() {
        overlay.classList.remove('visible');
    }

    window.warnIfInvalidChars = function (rawValue, cleanValue, fieldLabel = 'Campo') {
        if (rawValue === cleanValue) return false;

        // Trova i caratteri rimossi (unici)
        const removed = [...new Set(
            rawValue.split('').filter(ch => !cleanValue.includes(ch) || true)
        )].filter(ch => !cleanValue.split('').includes(ch));

        const uniqueRemoved = [...new Set(rawValue.replace(
            new RegExp('[' + cleanValue.replace(/[.*+?^${}()|[\]\\]/g, '\\$&').split('').join('') + ']', 'g'), ''
        ).split(''))].filter(ch => ch.trim() !== '' || ch === ' ');

        document.getElementById('invalid-char-msg').innerHTML =
            `Il campo <strong>${fieldLabel}</strong> contiene caratteri non consentiti.<br>Sono stati rimossi automaticamente.`;

        const list = document.getElementById('invalid-chars-list');
        list.innerHTML = '';

        const invalidFound = findInvalidChars(rawValue, cleanValue);
        if (invalidFound.length > 0) {
            invalidFound.forEach(ch => {
                const tag = document.createElement('span');
                tag.textContent = ch === ' ' ? 'spazio' : ch;
                list.appendChild(tag);
            });
            list.style.display = 'inline-flex';
        } else {
            list.style.display = 'none';
        }

        overlay.classList.add('visible');
        document.getElementById('invalid-char-close').focus();
        return true;
    };

    function findInvalidChars(raw, clean) {
        const cleanSet = new Set(clean.split(''));
        return [...new Set(raw.split('').filter(ch => !cleanSet.has(ch)))];
    }

})();