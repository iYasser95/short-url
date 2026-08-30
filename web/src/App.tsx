import {useState } from 'react'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL

function App() {
    const [url, setUrl] = useState('')
    const [shortUrl, setShortUrl] = useState('')
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')


    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
      event.preventDefault()

      setLoading(true)
      setError('')
      setShortUrl('')

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
    <main>
      <h1>Short URL</h1>

      <form onSubmit={handleSubmit}>
        <input
        type="url"
        value={url}
        onChange={(event) => setUrl(event.target.value)}
        />

        <button type ="submit" disabled={loading}>
          {loading ? 'Shortening...' : 'Shorten'}
        </button>

      </form>
      {shortUrl && (
        <p>
          Short URL: <a href={shortUrl}>{shortUrl}</a>

          <button
          type="button"
          onClick={() => navigator.clipboard.writeText(shortUrl)}>
            Copy
          </button>
        </p>
      )}
      {error && <p>{error}</p>}
    </main>
  )
}

export default App