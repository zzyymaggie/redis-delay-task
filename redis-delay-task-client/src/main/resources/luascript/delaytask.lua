if table.getn(ARGV) < 3
then
    ARGV[3] = 0 
end
if table.getn(ARGV) < 4
then
    ARGV[4] = 1
end
local res = {}
local idlist = redis.call("zrangebyscore", KEYS[1], ARGV[1], ARGV[2], "LIMIT", ARGV[3], ARGV[4])
if table.getn(idlist) > 0
then 
    for i=1, #idlist
    do
       res[i] = idlist[i]
       redis.call("zrem", KEYS[1], res[i])
    end
end
return res
