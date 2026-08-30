import {useState } from 'react'
import './App.css'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL

function App() {
    const [url, setUrl] = useState('')
    const [shortUrl, setShortUrl] = useState('')
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')
    const [copied, setCopied] = useState(false)


    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
      event.preventDefault()

      setLoading(true)
      setError('')
      setShortUrl('')
      setCopied(false)

      try {
              const response = await fetch(`${apiBaseUrl}/short-urls`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          url: url,
        }),
      })

      if (!response.ok) {
        throw new Error('Failed to shorten URL')
      }

      const data = await response.json()
      setShortUrl(data.shortUrl)
      } catch {
        setError('Something went wrong')
      } finally {
        setLoading(false)
      }
    }
  return (
    <main className="page">
      <section className="card">
      <h1>Short URL</h1>
      <p className="subtitle">
        Turn logn links into short, shareable URLs.
      </p>

      <form onSubmit={handleSubmit}>
        <input
        type="url"
        placeholder="https://example.com"
        value={url}
        onChange={(event) => setUrl(event.target.value)}
        required
        />

        <button type ="submit" disabled={loading || !url.trim()}>
          {loading ? 'Shortening...' : 'Shorten'}
        </button>

      </form>
      {shortUrl && (
        <div className="result">
          <div>
            <span className="result-label">Your short URL</span>
            <a href={shortUrl}>{shortUrl}</a>
          </div>

          <button
            type="button"
            onClick={async () => {
              await navigator.clipboard.writeText(shortUrl)
              setCopied(true)
            }}
          >
            {copied ? 'Copied' : 'Copy'}
          </button>
        </div>
      )}
      {error && (
        <div className="error">
          {error}
        </div>
      )}
      </section>
    </main>
  )
}

export default App