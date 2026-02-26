--KEYS[1] = rate limit key
--ARGV[1] = current timestamp (seconds)
--ARGV[2] = window size in (seconds)
--ARGV[3] = limit

local key = KEYS[1]
local now = tonumber(ARGV[1])
local windowSize = tonumber(ARGV[2])
local limit = tonumber(ARGV[3])

-- remove expired entries
redis.call('ZREMRANGEBYSCORE', key, 0, now - windowSize)

--current count
local current = redis.call('ZCARD', key)

if current < limit then
    --add request with unique value
    redis.call('ZADD', key, now, now .. '-' .. math.random())
    redis.call('EXPIRE', key, windowSize)
    return {1, limit, limit - current - 1}
else
    return {0,0}
end