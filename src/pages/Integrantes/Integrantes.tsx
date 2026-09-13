import Card from '../../components/ui/Card';
import { integrantesMock } from '../../data/integrantes';

// Página Integrantes — mostra os cartões dos membros da equipe
export default function Integrantes() {
  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
      <h1 className="text-3xl sm:text-4xl font-bold text-dark-50 mb-2">Integrantes</h1>
      <p className="text-dark-400 mb-8">A equipe por trás do SoulUp</p>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {integrantesMock.map((integrante) => (
          <Card key={integrante.id} className="text-center">
            {/* Foto placeholder — iniciais do nome em um círculo colorido */}
            <div className="w-20 h-20 rounded-full bg-aqua-500/10 flex items-center justify-center mx-auto mb-4">
              <span className="text-2xl font-bold text-aqua-400">
                {integrante.nome.charAt(0)}
              </span>
            </div>

            <h3 className="text-base font-semibold text-dark-100 mb-1">{integrante.nome}</h3>
            <p className="text-xs text-dark-400 mb-1">RM: {integrante.rm}</p>
            <p className="text-xs text-dark-400 mb-4">{integrante.turma}</p>

            {/* Links GitHub e LinkedIn */}
            <div className="flex justify-center gap-4">
              <a
                href={integrante.github}
                target="_blank"
                rel="noopener noreferrer"
                className="text-dark-400 hover:text-aqua-400 transition-colors"
                aria-label="GitHub"
              >
                <svg className="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.605-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.37 1.23-3.21-.12-.3-.54-1.515.12-3.15 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.635.24 2.85.12 3.15.765.84 1.23 1.895 1.23 3.21 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0 0 24 12c0-6.63-5.37-12-12-12z" />
                </svg>
              </a>
              <a
                href={integrante.linkedin}
                target="_blank"
                rel="noopener noreferrer"
                className="text-dark-400 hover:text-aqua-400 transition-colors"
                aria-label="LinkedIn"
              >
                <svg className="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433a2.062 2.062 0 0 1-2.063-2.065 2.063 2.063 0 1 1 2.063 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z" />
                </svg>
              </a>
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
}
